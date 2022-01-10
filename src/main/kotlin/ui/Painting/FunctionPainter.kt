package ui.Painting

import CartesianPlane
import java.awt.*

class FunctionPainter(var function: (Double)->Double,val plane: CartesianPlane) : Painter
{

    var funColor: Color = Color.BLUE

    override fun paint(g: Graphics){
        with(g as Graphics2D) {
            color = funColor
            stroke = BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            val rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            setRenderingHints(rh)
            with(plane) {
                for (i in 0 until width) {
                    drawLine(
                        i,
                        yCrt2Scr(function(xScr2Crt(i))),
                        i + 1,
                        yCrt2Scr(function(xScr2Crt(i + 1))),
                    )
                }
            }

        }
    }
}