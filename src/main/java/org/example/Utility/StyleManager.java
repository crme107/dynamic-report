package org.example.Utility;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.*;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class StyleManager {
    public static StyleBuilder titleStyle = stl.style()
            .setFontSize(22)
            .setLeftIndent(15)
            .setTopPadding(20)
            .setBottomPadding(20)
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

    public static StyleBuilder imageStyle = stl.style()
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);

    public static StyleBuilder columnTitleStyle = stl.style()
            .setFontSize(13)
            .setPadding(6)
            .setBackgroundColor(Color.decode("#00AEEF"))
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

    public static StyleBuilder columnStyle = stl.style()
            .setPadding(4)
            .setLeftIndent(10)
            .setRightIndent(10)
            .setBorder(stl.pen1Point())
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

    public static StyleBuilder crosstabStyle = stl.style()
            .setTopPadding(20)
            .setLeftPadding(20)
            .setRightPadding(20)
            .setBottomPadding(20);

    public static StyleBuilder verticalListStyle = stl.style()
            .setTopPadding(50)
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
}
