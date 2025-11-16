package view;

import geometry.Coordinates;
import geometry.functions.ScanLine;
import geometry.shape.Edge;
import geometry.shape.Polygon;
import raster.Canvas;
import raster.CanvasPreview;
import raster.Pixel;
import util.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public class Controller2D extends JPanel implements MouseListener, MouseMotionListener {

    private Canvas canvas;
    private CanvasPreview previewCanvas;

    private Edge tempNearestEdge;

    private final Deque<Canvas> history = new ArrayDeque<>();

    private Polygon selectedPolygon;
    private Coordinates selectedVertex;
    private final double vertexMarkerRadius = 6;

    private final int imgWidth;
    private final int imgHeight;

    // poslední známá pozice myši pro G
    private int lastMouseX;
    private int lastMouseY;

    public Controller2D(int width, int height) {
        this.imgWidth = width;
        this.imgHeight = height;

        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(Colors.BACKGROUND));

        addMouseListener(this);
        addMouseMotionListener(this);

        resetCanvas();
        redrawCanvas();
        resetPreview();

        // aby panel bral klávesnici
        setFocusable(true);
        requestFocusInWindow();

        // klávesové zkratky: G = ořez + seedfill, H = scanline fill
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char ch = e.getKeyChar();

                // G = stejné chování jako prostřední tlačítko: ořez + seed fill
                if (ch == 'g' || ch == 'G') {

                    // bezpečnost: musí existovat oba polygony pro ořez
                    if (canvas.getPolygon().numberOfVertices() < 3) return;
                    if (canvas.getClippingPolygon().numberOfVertices() < 3) return;

                    // poslední pozice myši musí být v plátně
                    if (lastMouseX < 0 || lastMouseY < 0
                            || lastMouseX >= imgWidth || lastMouseY >= imgHeight) return;

                    // 1) ořez polygonu
                    clip();

                    // 2) překreslit polygony na nový canvas (bez vyplnění)
                    redrawCanvas();

                    // 3) seed fill dovnitř (už žádný další redraw!)
                    try {
                        canvas.fill(canvas.getPixel(lastMouseX, lastMouseY));
                    } catch (IndexOutOfBoundsException ex) {
                        // ignore
                    }

                    repaint();

                    // H = scanline fill hlavního polygonu (bez klipearu, čistě podle polygonu)
                } else if (ch == 'h' || ch == 'H') {

                    // musí existovat hlavní polygon se 3+ vrcholy
                    if (canvas.getPolygon().numberOfVertices() < 3) return;

                    // vezmeme všechny fillovací pixely ze ScanLine a obarvíme je FILL barvou
                    ScanLine.getFill(canvas.getPolygon())
                            .forEach(coord -> canvas.drawPixel(
                                    new Pixel((int) coord.getX(), (int) coord.getY(), Colors.FILL)
                            ));

                    // polygonové hrany zůstanou, jen se "pod ně" dobarví vnitřek
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (canvas != null) {
            g.drawImage(canvas, 0, 0, null);
        }

        if (previewCanvas != null) {
            g.drawImage(previewCanvas, 0, 0, null);
        }

        if (selectedVertex != null) {
            int r = (int) vertexMarkerRadius;
            int x = (int) (selectedVertex.getX() - r);
            int y = (int) (selectedVertex.getY() - r);

            g.setColor(Color.RED);
            g.drawOval(x, y, 2 * r, 2 * r);
        }
    }

    private void resetPreview() {
        previewCanvas = new CanvasPreview(imgWidth, imgHeight);
        repaint();
    }

    private void resetCanvas() {
        canvas = new Canvas(imgWidth, imgHeight);
    }

    private void redrawCanvas() {
        history.add(canvas);

        Polygon polygon = new Polygon(canvas.getPolygon());
        Polygon clippingPolygon = new Polygon(canvas.getClippingPolygon());

        resetCanvas();
        canvas.setPolygon(polygon);
        canvas.setClippingPolygon(clippingPolygon);

        canvas.draw();
        repaint();
    }

    private void clip() {
        Polygon clipped = canvas.getClippedPolygon();
        if (clipped == null) return;

        // když by byl průnik prázdný / degenerovaný, tak neměň polygon
        if (clipped.numberOfVertices() < 3) {
            return;
        }

        canvas.setPolygon(clipped);
        canvas.setClippingPolygon(new Polygon());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Coordinates currentCoordinates = new Coordinates(e.getX(), e.getY());

        lastMouseX = e.getX();
        lastMouseY = e.getY();

        if (SwingUtilities.isLeftMouseButton(e)) {

            if (canvas.getPolygon().numberOfVertices() == 0) {
                canvas.getPolygon().addVertex(currentCoordinates);
            }
            tempNearestEdge = canvas.getPolygon()
                    .getCounterClockwise()
                    .getNearestEdge(currentCoordinates);

        } else if (SwingUtilities.isMiddleMouseButton(e)) {

            // 1) ořez
            clip();

            // 2) překreslit polygony
            redrawCanvas();

            // 3) fill z místa kliknutí
            try {
                canvas.fill(canvas.getPixel(e.getX(), e.getY()));
            } catch (IndexOutOfBoundsException ex) {
            }

            repaint();

        } else if (SwingUtilities.isRightMouseButton(e)) {

            if (canvas.getClippingPolygon().numberOfVertices() == 0) {
                canvas.getClippingPolygon().addVertex(currentCoordinates);
            }
            tempNearestEdge = canvas.getClippingPolygon()
                    .getCounterClockwise()
                    .getNearestEdge(currentCoordinates);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Coordinates currentCoordinates = new Coordinates(e.getX(), e.getY());

        if (SwingUtilities.isLeftMouseButton(e)) {
            resetPreview();
            canvas.getPolygon().insertVertex(currentCoordinates, tempNearestEdge);
            redrawCanvas();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            resetPreview();
            canvas.getClippingPolygon().insertVertex(currentCoordinates, tempNearestEdge);
            redrawCanvas();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();

        Coordinates currentCoordinates = new Coordinates(e.getX(), e.getY());

        if (SwingUtilities.isLeftMouseButton(e) || SwingUtilities.isRightMouseButton(e)) {
            resetPreview();
            if (tempNearestEdge != null) {
                previewCanvas.drawPreviewLine(currentCoordinates, tempNearestEdge.getPointA());
                previewCanvas.drawPreviewLine(currentCoordinates, tempNearestEdge.getPointB());
                repaint();
            }
        }

        if (selectedVertex != null && SwingUtilities.isLeftMouseButton(e)) {
            resetPreview();
            Coordinates current = new Coordinates(e.getX(), e.getY());
            Coordinates pred = selectedPolygon.getPredecessingVertex(selectedVertex);
            Coordinates succ = selectedPolygon.getSuccessingVertex(selectedVertex);
            previewCanvas.drawPreviewLine(current, pred);
            previewCanvas.drawPreviewLine(current, succ);
            selectedVertex = current;
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();

        Coordinates currentCoordinates = new Coordinates(e.getX(), e.getY());

        selectedVertex = Stream.concat(
                        canvas.getPolygon().getVertices().stream(),
                        canvas.getClippingPolygon().getVertices().stream()
                ).filter(vertex -> currentCoordinates.calculateDistance(vertex) < vertexMarkerRadius)
                .findAny()
                .orElse(null);

        if (selectedVertex != null) {
            boolean inMain = canvas.getPolygon()
                    .getEdges()
                    .stream()
                    .anyMatch(edge -> edge.hasVertex(selectedVertex));

            selectedPolygon = inMain ? canvas.getPolygon() : canvas.getClippingPolygon();
        } else {
            selectedPolygon = null;
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && selectedPolygon != null && selectedVertex != null) {
            selectedPolygon.deleteVertex(selectedVertex);
            selectedVertex = null;
            selectedPolygon = null;
            redrawCanvas();
        }
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
