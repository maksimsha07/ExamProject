package ui

import CartesianPlane
import Math.FunctionToPolynom
import ui.Painting.CartesianPainter
import ui.Painting.FunctionPainter
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import ui.Painting.Painter
import ui.Painting.ParametricFunPainter
import kotlin.math.*
import kotlin.math.sin

class MainFrame : JFrame(){
    val mainPanel: GraphicsPanel
    val panelColorFun: JPanel = JPanel()
    val panelColorFunParam: JPanel = JPanel()
    val controlPanel: JPanel
    var showfun = JCheckBox("Показать явный")
    var showFunParam = JCheckBox("Показать параметрчиеский")
    val xMin: JSpinner
    val xMinM: SpinnerNumberModel
    val xMax: JSpinner
    val xMaxM: SpinnerNumberModel
    val yMin: JSpinner
    val yMinM: SpinnerNumberModel
    val yMax: JSpinner
    val yMaxM: SpinnerNumberModel
    val tMin: JSpinner
    val tMinM: SpinnerNumberModel
    val tMax: JSpinner
    val tMaxM: SpinnerNumberModel
    private val xMinLabel: JLabel = JLabel("xMin")
    private val xMaxLabel: JLabel = JLabel("xMax")
    private val yMinLabel: JLabel = JLabel("yMin")
    private val yMaxLabel: JLabel = JLabel("yMax")
    private val tMinLabel: JLabel = JLabel("tMin")
    private val tMaxLabel: JLabel = JLabel("tMax")
    val panelSize = 20
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(1000, 800)
        xMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        xMin = JSpinner(xMinM)
        xMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        xMax = JSpinner(xMaxM)
        yMinM = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        yMin = JSpinner(yMinM)
        yMaxM = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        yMax = JSpinner(yMaxM)
        tMinM = SpinnerNumberModel(0.0, -100.0, 4.9, 0.1)
        tMin = JSpinner(tMinM)
        tMaxM = SpinnerNumberModel(10.0, -4.9, 100.0, 0.1)
        tMax = JSpinner(tMaxM)

        val mainPlane = CartesianPlane(
            xMinM.value as Double,
            xMaxM.value as Double,
            yMinM.value as Double,
            yMaxM.value as Double
        )

        mainPlane.tMax = tMaxM.value as Double
        mainPlane.tMin = tMinM.value as Double

        //Плоскость
        val cartesianPainter = CartesianPainter(mainPlane)


        //Задание
        val funk = { x:Double ->  abs(2*x-x.pow(2)) } //Функция
        val x = {t:Double -> (4-t.pow(2))/(1+t.pow(3))} //параметрически заданная
        val y = {t:Double -> (t.pow(2))/(1+t.pow(3))} // функция

        //Пул функция рисования
        val painters = mutableListOf<Painter>(cartesianPainter)

        //Сами функции рисования
        val pm = ParametricFunPainter(x,y,mainPlane)
        val FunPainter = FunctionPainter(funk,mainPlane)

        //Добавление графиков
        painters.add(pm)
        painters.add(FunPainter)

        panelColorFun.background = FunPainter.funColor
        panelColorFunParam.background = pm.funColor


        showfun.isSelected = true
        showFunParam.isSelected = true





        mainPanel = GraphicsPanel(painters).apply {
            background = Color.WHITE
        }


        mainPlane.pixelSize = mainPanel.size

        mainPanel.addComponentListener(object: ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                mainPlane.pixelSize = mainPanel.size
                mainPanel.repaint()
            }
        })



        showfun.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(showfun.isSelected){
                    painters.add(FunPainter)
                    showfun.isSelected = true
                }
                else{
                    painters.remove(FunPainter)
                    showfun.isSelected = false
                }
                mainPanel.repaint()
            }
        })

        showFunParam.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                if(showFunParam.isSelected){
                    painters.add(pm)
                    showFunParam.isSelected = true
                }
                else{
                    painters.remove(pm)
                    showFunParam.isSelected = false
                }
                mainPanel.repaint()
            }
        })





        panelColorFun.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?)
            {
                val background = JColorChooser.showDialog(null, "JColorChooser Sample", Color.BLACK)
                panelColorFun.background = background
                FunPainter.funColor = background
                mainPanel.repaint()
            }
        })
        panelColorFunParam.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?)
            {
                val background = JColorChooser.showDialog(null, "JColorChooser Sample", Color.BLACK)
                panelColorFunParam.background = background
                pm.funColor = background
                mainPanel.repaint()
            }
        })

        controlPanel = JPanel().apply{
        }
        layout = GroupLayout(contentPane).apply{
            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                            .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    )
                    .addGap(4)
            )

            setVerticalGroup(
                createSequentialGroup()
                    .addGap(4)
                    .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addGap(4)
                    .addComponent(controlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(4)
            )
        }

        xMin.addChangeListener{
            xMaxM.minimum = xMin.value as Double + 0.1
            mainPlane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        xMax.addChangeListener{
            xMinM.maximum = xMax.value as Double - 0.1
            mainPlane.xSegment = Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        yMin.addChangeListener{
            yMaxM.minimum = yMin.value as Double + 0.1
            mainPlane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        yMax.addChangeListener{
            yMinM.maximum = yMax.value as Double - 0.1
            mainPlane.ySegment = Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        tMin.addChangeListener{
            tMaxM.minimum = tMin.value as Double + 0.1
            mainPlane.tMax = tMax.value as Double
            mainPlane.tMin = tMin.value as Double
            mainPanel.repaint()
        }
        tMax.addChangeListener{
            tMinM.maximum = tMax.value as Double - 0.1
            mainPlane.tMax = tMax.value as Double
            mainPlane.tMin = tMin.value as Double
            mainPanel.repaint()
        }

        controlPanel.layout = GroupLayout(controlPanel).apply {
            setVerticalGroup(createSequentialGroup()
                .addGap(10)
                .addGroup(createParallelGroup()
                    .addComponent(xMinLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(xMin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(xMaxLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(xMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(tMinLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(showfun,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(panelColorFun,panelSize,panelSize,panelSize)


                ).addGap(10)
                .addGroup(createParallelGroup()
                    .addComponent(yMinLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yMin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(yMaxLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(tMaxLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(showFunParam,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(panelColorFunParam,panelSize,panelSize,panelSize)

                ).addGap(10)
            )
            setHorizontalGroup(createSequentialGroup()
                .addGap(20)
                .addGroup(createParallelGroup()
                    .addComponent(xMinLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yMinLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(10)
                .addGroup(createParallelGroup()
                    .addComponent(xMin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(yMin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)

                ).addGap(100)
                .addGroup(createParallelGroup()
                    .addComponent(xMaxLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(yMaxLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(10)
                .addGroup(createParallelGroup()
                    .addComponent(xMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(yMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                ).addGap(100)
                .addGroup(createParallelGroup()
                    .addComponent(tMinLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMaxLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(10)
                .addGroup(createParallelGroup()
                    .addComponent(tMin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(tMax, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)

                ).addGap(20)
                .addGroup(createParallelGroup()
                    .addComponent(showfun,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(showFunParam,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)

                ).addGap(20)
                .addGroup(createParallelGroup()
                    .addComponent(panelColorFun,panelSize,panelSize,panelSize)
                    .addComponent(panelColorFunParam,panelSize,panelSize,panelSize)

                )

            )

        }
        pack()
        mainPlane.width = mainPanel.width
        mainPlane.height = mainPanel.height

    }
}