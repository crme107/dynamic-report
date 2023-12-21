package org.example.dynamicreport.Export;

import net.sf.dynamicreports.jasper.builder.export.JasperImageExporterBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.jasper.constant.ImageType;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ImageBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import org.example.dynamicreport.Utility.ConnectionManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import static org.example.dynamicreport.Utility.Styles.*;

public class DynamicReport {
    private static final String REPORT_TITLE = "Holidays";
    private static final String CHART_TITLE = "Holidays each month";

    private static final String IMG_SOURCE_PATH = "src/main/resources/cedacri.png";
    private static final String EXPORTED_PDF_FILENAME = "DynamicHolidays";
    private static final String EXPORTED_PDF_TYPE = ".pdf";
    private static final String EXPORTED_IMAGE_FILENAME = "DynamicHolidays";
    private static final ImageType IMAGE_TYPE = ImageType.PNG;

    private static final int IMG_WIDTH = 190;
    private static final int IMG_HEIGHT = 62;
    private static final int VERTICAL_LIST_GAP = 40;
    private static final int CROSSTAB_CELL_WIDTH = 30;
    private static final boolean SHOW_BAR_CHART_VALUES = true;

    private static final String QUERY_HOLIDAYS = "select holiday_name as name, holiday_date as date, country from holiday";
    private static final String QUERY_CROSSTAB = "select MONTH(STR_TO_DATE(holiday_date, '%d/%m/%Y')) as month, country from holiday;";
    private static final String QUERY_CHART = "select country, MONTHNAME(STR_TO_DATE(holiday_date, '%d/%m/%Y')) as month, COUNT(MONTH(STR_TO_DATE(holiday_date, '%d/%m/%Y'))) as count from holiday group by month, country;";

    public DynamicReport(String path) {
        build(path);
    }

    private void build(String path) {
        createReport(path);
    }

    private static void createReport(String path) {
        JasperPdfExporterBuilder pdfExporter = export.pdfExporter(path + EXPORTED_PDF_FILENAME + EXPORTED_PDF_TYPE);
        JasperImageExporterBuilder imgExporter = export.imageExporter(path + EXPORTED_IMAGE_FILENAME, IMAGE_TYPE);

        try (Connection connection = ConnectionManager.createConnection()) {
            report().columns(
                            col.column("Name", "name", type.stringType()).setStyle(columnStyle),
                            col.column("Date", "date", type.stringType()).setStyle(columnStyle),
                            col.column("Country", "country", type.stringType()).setStyle(columnStyle)
                    )
                    .setColumnTitleStyle(columnTitleStyle)
                    .title(cmp.horizontalList()
                            .add(cmp.text(REPORT_TITLE).setStyle(titleStyle))
                            .add(createImage().setStyle(imageStyle))
                    )
                    .setDataSource(createDataSource(connection, QUERY_HOLIDAYS))
                    .summaryOnANewPage()
                    .summary(cmp.verticalList().setGap(VERTICAL_LIST_GAP).setStyle(verticalListStyle)
                            .add(createBarChart(connection))
                            .add(createCrosstab(connection).setStyle(crosstabStyle))
                    )
                    .toImage(imgExporter)
                    .toPdf(pdfExporter);

            String pdf = EXPORTED_PDF_FILENAME + EXPORTED_PDF_TYPE;
            String img = EXPORTED_IMAGE_FILENAME + '.' + IMAGE_TYPE;
            System.out.println(pdf + " and " + img + " have been exported successfully in " + path);
        } catch (SQLException | DRException e) {
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

    private static ImageBuilder createImage() {
        try {
            BufferedImage img = ImageIO.read(new File(IMG_SOURCE_PATH));
            return cmp.image(img).setFixedDimension(IMG_WIDTH, IMG_HEIGHT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CrosstabBuilder createCrosstab(Connection connection) {
        CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("country", String.class);
        CrosstabColumnGroupBuilder<Integer> columnGroup = ctab.columnGroup("month", Integer.class);

        return ctab.crosstab()
                .setDataSource(createDataSource(connection, QUERY_CROSSTAB))
                .rowGroups(rowGroup).setCellWidth(CROSSTAB_CELL_WIDTH)
                .columnGroups(columnGroup)
                .measures(ctab.measure("month", Integer.class, Calculation.COUNT));
    }

    private static BarChartBuilder createBarChart(Connection connection) {
        TextColumnBuilder<String> countryColumn = col.column("Country", "country", type.stringType());
        TextColumnBuilder<String> monthColumn = col.column("Month", "month", type.stringType());
        TextColumnBuilder<Integer> countColumn = col.column("Count", "count", type.integerType());

        return cht.barChart()
                .setDataSource(createDataSource(connection, QUERY_CHART))
                .setTitle(CHART_TITLE)
                .setCategory(monthColumn)
                .series(cht.serie(countColumn).setSeries(countryColumn))
                .setShowValues(SHOW_BAR_CHART_VALUES);
    }
}