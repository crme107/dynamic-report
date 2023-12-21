package org.example.dynamicreport.Utility;

import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.*;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

public class Styles {
    private static final String TABLE_HEADER_COLOR = "#CFD9E6";

    public static StyleBuilder titleStyle = stl.style()
            .setFontSize(22)
            .setTopPadding(20)
            .setBottomPadding(20)
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

    public static StyleBuilder imageStyle = stl.style()
            .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE)
            .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);

    public static StyleBuilder columnTitleStyle = stl.style()
            .setFontSize(13)
            .setPadding(4)
            .setBackgroundColor(Color.decode(TABLE_HEADER_COLOR))
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

    public static StyleBuilder columnStyle = stl.style()
            .setPadding(3)
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
