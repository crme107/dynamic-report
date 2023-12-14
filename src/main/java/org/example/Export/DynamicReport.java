package org.example.Export;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import org.example.Utility.ConnectionManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import static org.example.Utility.StyleManager.*;

public class DynamicReport {
    public static final String QUERY_HOLIDAYS = "select holiday_name as name, holiday_date as date, country from holiday";
    public static final String QUERY_CROSSTAB = "select MONTH(STR_TO_DATE(holiday_date, '%d/%m/%Y')) as month, country from holiday;";
    public static final String QUERY_CHART = "select country, MONTHNAME(STR_TO_DATE(holiday_date, '%d/%m/%Y')) as month, COUNT(MONTH(STR_TO_DATE(holiday_date, '%d/%m/%Y'))) as count from holiday group by month, country;";

    public DynamicReport(String path) {
        build(path);
    }

    private void build(String path) {
        try (Connection connection = ConnectionManager.createConnection()) {
            JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path + "DynamicHolidays.pdf");
            BufferedImage img = ImageIO.read(new File("src/main/resources/cedacri.png"));

            CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("country", String.class);
            CrosstabColumnGroupBuilder<Integer> columnGroup = ctab.columnGroup("month", Integer.class);

            CrosstabBuilder crosstab = ctab.crosstab()
                    .setDataSource(createDataSource(connection, QUERY_CROSSTAB))
                    .rowGroups(rowGroup).setCellWidth(30)
                    .columnGroups(columnGroup)
                    .measures(ctab.measure("month", Integer.class, Calculation.COUNT));

            TextColumnBuilder<String> countryColumn = col.column("Country", "country", type.stringType());
            TextColumnBuilder<String> monthColumn = col.column("Month", "month", type.stringType());
            TextColumnBuilder<Integer> countColumn = col.column("Count", "count", type.integerType());

            report().columns(
                    col.column("Name", "name", type.stringType()).setStyle(columnStyle),
                    col.column("Date", "date", type.stringType()).setStyle(columnStyle),
                    col.column("Country", "country", type.stringType()).setStyle(columnStyle)
                )
                .setColumnTitleStyle(columnTitleStyle)
                .title(cmp.horizontalList()
                        .add(cmp.text("Holidays").setStyle(titleStyle))
                        .add(cmp.image(img).setFixedDimension(190, 62).setStyle(imageStyle))
                )
                .setDataSource(createDataSource(connection, QUERY_HOLIDAYS))
                .summaryOnANewPage()
                .summary(cmp.verticalList().setGap(40).setStyle(verticalListStyle)
                        .add(cht.barChart()
                                .setDataSource(createDataSource(connection, QUERY_CHART))
                                .setTitle("Holidays each month")
                                .setCategory(monthColumn)
                                .series(cht.serie(countColumn).setSeries(countryColumn))
                                .setShowValues(true))
                        .add(crosstab.setStyle(crosstabStyle))
                )
                .toPdf(pdfExporter);

            System.out.println("'DynamicHolidays.pdf' was exported successfully in the 'resources' directory.");
        } catch (DRException | IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static JRDataSource createDataSource(Connection connection, String query) {
        try {
            PreparedStatement prepareStatement = connection.prepareStatement(query);
            return new JRResultSetDataSource(prepareStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}