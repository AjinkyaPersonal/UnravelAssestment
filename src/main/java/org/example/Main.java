package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        try {
            HtmlToPdfConverter();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void HtmlToPdfConverter() throws IOException {

        Document document = modificationOnHtmlFile();

        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        try (FileOutputStream oStream = new FileOutputStream("C:/Users/hp/Desktop/Documents/ITN3.pdf")) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext cntContext = renderer.getSharedContext();
            cntContext.setPrint(true);
            cntContext.setInteractive(false);
            String baseString = FileSystems.getDefault().getPath("C:/Users/hp/Desktop/Documents").toUri().toURL().toString();
            renderer.setDocumentFromString(document.html(), baseString);
            renderer.layout();

            renderer.createPDF(oStream);
            System.out.print("Done");
        }

        catch (Exception e) {
            System.out.print("Error" + e.getMessage());
        }
    }


    private static Document modificationOnHtmlFile() throws IOException {

        File htmlfile = new File("C:/Users/hp/IdeaProjects/UnRavel/untitled/src/main/resources/file1.html");

        Document document = Jsoup.parse(htmlfile, "UTF-8");


        Element removeAgent = document.selectFirst("table");

        if (removeAgent != null) {

            Elements rows = removeAgent.select("tr");

            int targetRow = 1;
            int targetColumn = 2;

            Element tdElement = rows.get(targetRow).select("td").get(targetColumn);

            targetRow = 1;
            targetColumn = 1;

            Element tdElement2 = rows.get(targetRow).select("td").get(targetColumn);
            tdElement.remove();
            tdElement2.remove();


        }




        Element removePassengerDetails = document.select("table#voucherPanel").first();
        if (removePassengerDetails != null) {
            for (Element tr : removePassengerDetails.select("tr")) {
                Element passenderDetailElement = tr.selectFirst("td");
                if (passenderDetailElement.text().equals("PASSENGER DETAILS")) {
                    passenderDetailElement.text("GUEST & STAY DETAILS");
                }
            }
        }


        Element table = document.select("table#passengerDetails").first();

        if (table != null) {
            for (Element tr : table.select("tr")) {

                Element GuestName = tr.selectFirst("td");
                Element PassengerNationality = tr.selectFirst("td");
                if (GuestName.text().equals("Passenger Name")) {
                    GuestName.text("Guest Name");
                }
                if (PassengerNationality.text().equals("Passenger Nationality")) {
                    PassengerNationality.text("Guest Nationality");
                }
            }
        }

        Element tables2 = document.select("table#voucherGeneralDetails").first();

        for (Element tr : tables2.select("tr")) {

            Element image = tr.selectFirst("img");
            if (image != null) {
                image.attr("src",
                        "https://mcusercontent.com/b9b38543e81e56f3d1e9fc377/_thumbs/a605dd62-c37f-590a-dc11-bbbbf4ad29f1.png");
            }

        }

        for (Element tr : tables2.select("tr")) {
            Element itinerayNumber = tr.selectFirst("td");
            Element referenceNumber = tr.selectFirst("td");
            if (itinerayNumber.text().equals("Itinerary Number")) {
                tr.remove();
            }
            if (referenceNumber.text().equals("Reference Number")) {
                tr.remove();
            }

        }

        Elements strongElement = document.select("strong");
        for (Element strong : strongElement) {

            if (strong.text().equals("Operations Team dummyvendor FZ LLC")) {
                strong.text("Unravel Support");
            }
        }

        Element anchorElement = document.selectFirst("a");
        if (anchorElement != null) {
            anchorElement.attr("href", "support@gounravel.com");
            anchorElement.text("support@gounravel.com");
        }

        return document;
    }


}