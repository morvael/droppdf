/*
 * Copyright (C) 2020 domin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.derwinski.droppdf;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import pl.derwinski.droppdf.json.Faction;
import pl.derwinski.droppdf.json.Unit;
import pl.derwinski.droppdf.json.Weapon;
import pl.derwinski.download.DownloadMode;
import pl.derwinski.download.Downloader;
import pl.derwinski.pdf.BlockBuilder;
import pl.derwinski.pdf.Border;
import pl.derwinski.pdf.CellBuilder;
import pl.derwinski.pdf.HAlign;
import pl.derwinski.pdf.ParaBuilder;
import pl.derwinski.pdf.PdfMaker;
import static pl.derwinski.pdf.PdfMaker.mmToPt;
import pl.derwinski.pdf.TableBuilder;
import pl.derwinski.pdf.VAlign;

/**
 *
 * @author Dominik Derwiński
 */
public class DropPDF {

    private static final DownloadMode DOWNLOAD_MODE = DownloadMode.MISSING;

    private final Downloader downloader;
    private final JsonMapper mapper;

    private FactionData fd;
    private BaseFont ss;
    private BaseFont lsr;
    private BaseFont lsi;
    private Font titleFont;
    private Font loreFont;
    private Font gamePlayFont;
    private Font revFont;
    private Font categoryFont;
    private Font unitNameFont;
    private Font unitLabelFont;
    private Font unitFont;
    private Font transportFont;
    private Font squadSizeFont;
    private Font ruleTitleFont;
    private Font ruleFont;

    public DropPDF() throws Exception {
        downloader = new Downloader(new File("."), DOWNLOAD_MODE);
        mapper = new JsonMapper();
    }

    public void run() throws Exception {
        Faction[] factions = mapper.readValue(downloader.download(Resource.FACTIONS.getURL(), "factions.json"), Faction[].class);
        for (Faction f : factions) {
            fd = FactionData.valueOf(f.Name);
            try (PdfMaker pdf = new PdfMaker(downloader.getFile("DZC_%s_A4.pdf", f.Name), PageSize.A4)) {
                writeDZC(pdf, f);
            }
            try (PdfMaker pdf = new PdfMaker(downloader.getFile("DZC_%s_Letter.pdf", f.Name), PageSize.LETTER)) {
                writeDZC(pdf, f);
            }
        }
    }

    private void writeDZC(PdfMaker pdf, Faction f) throws Exception {
        fd = FactionData.valueOf(f.Name);
        ss = pdf.getBaseFont("/Squarish Sans CT Regular SC.ttf");
        lsr = pdf.getBaseFont("/LiberationSerif-Regular.ttf");
        lsi = pdf.getBaseFont("/LiberationSerif-Italic.ttf");
        titleFont = pdf.getFont(ss, 60, Color.BLACK);
        loreFont = pdf.getFont(lsi, 12, Color.BLACK);
        gamePlayFont = pdf.getFont(lsr, 12, Color.BLACK);
        revFont = pdf.getFont(lsr, 8, Color.BLACK);
        categoryFont = pdf.getFont(ss, 14, fd.getTextColor());
        unitNameFont = pdf.getFont(ss, 9, fd.getTextColor());
        unitLabelFont = pdf.getFont(ss, 8, fd.getTextColor());
        unitFont = pdf.getFont(ss, 8, Color.BLACK);
        transportFont = pdf.getFont(ss, 10, Color.BLACK);
        squadSizeFont = pdf.getFont(ss, 14, Color.BLACK);
        ruleTitleFont = pdf.getFont(ss, 12, Color.BLACK);
        ruleFont = pdf.getFont(lsr, 9, Color.BLACK);

        File factionLogoFile = downloader.download(Resource.getAnyURL(f.Imageurl), "%s.%s", f.Name, Resource.getExtension(f.Imageurl));

        pdf.newBlock()
                .add(pdf.newImage()
                        .source(factionLogoFile)
                        .scaleToFit(mmToPt(30f), mmToPt(30f))
                        .hAlign(HAlign.CENTER)
                        .build())
                .add(pdf.newPara()
                        .add(f.Name, titleFont)
                        .hAlign(HAlign.CENTER)
                        .build())
                .add(pdf.newPara()
                        .add(f.Lore, loreFont)
                        .hAlign(HAlign.JUSTIFIED)
                        .hyphenation(true)
                        .build())
                .add(pdf.newPara()
                        .add(f.GamePlay, gamePlayFont)
                        .hAlign(HAlign.JUSTIFIED)
                        .hyphenation(true)
                        .build())
                .add(pdf.newPara()
                        .add(revFont, "Revision: %s", f._rev)
                        .hAlign(HAlign.CENTER)
                        .build())
                .paddingTop(mmToPt(2f))
                .paddingBottom(mmToPt(2f))
                .blockAlign(VAlign.MIDDLE)
                .endPage(true)
                .buildAndAdd();

        Unit[] units = mapper.readValue(downloader.download(Resource.UNITS.getURL(f.Name), "%s_units.json", f.Name), Unit[].class);

        writeUnits(pdf, f, "Command", units, "Command");
        writeUnits(pdf, f, "Standard", units, "Standard");
        writeUnits(pdf, f, "Heavy", units, "Heavy");
        writeUnits(pdf, f, "Support", units, "Support");
        writeUnits(pdf, f, "Troops", units, "Troops");
        writeUnits(pdf, f, "Exotic", units, "Exotic");
        writeUnits(pdf, f, "Scout", units, "Scout");
        writeUnits(pdf, f, "Auxiliary & Transports", units, "Auxiliary", "Airstrike", "Transport");
        writeUnits(pdf, f, "Extras", units, "Extras");
    }

    private void writeUnits(PdfMaker pdf, Faction f, String title, Unit[] units, String... category) throws Exception {
        ArrayList<Unit> list = getUnits(units, category);
        if (list.isEmpty()) {
            return;
        }

        pdf.newTable()
                .add(pdf.newCell()
                        .content(title, categoryFont)
                        .hAlign(HAlign.CENTER)
                        .vAlign(VAlign.MIDDLE)
                        .backgroundColor(fd.getColor())
                        .border(Border.BOX)
                        .padding(mmToPt(2f))
                        .build())
                .spacingAfter(mmToPt(10f))
                .buildAndAdd();

        for (Unit u : list) {
            writeUnit(pdf, f, u);
        }

        pdf.getDocument().newPage();
    }

    private void writeUnit(PdfMaker pdf, Faction f, Unit u) throws Exception {
        File unitPhotoFile = downloader.download(Resource.UNIT_PHOTOS.getURL(f.Name, u.Name), "%s_%s.jpg", f.Name, u.Name);

        BlockBuilder block = pdf.newBlock()
                .paddingTop(mmToPt(2f))
                .paddingBottom(mmToPt(2f))
                .keepTogether(true)
                .spacingAfter(mmToPt(10f));

        TableBuilder topTable = pdf.newTable()
                .numColumns(7)
                .relativeWidths(23f, 14f, 15.5f, 14f, 14.5f, 19.5f, 25f)
                .keepTogether(true);

        topTable.add(pdf.newCell()
                .content(pdf.newImage()
                        .source(unitPhotoFile)
                        .scaleToFit(mmToPt(25f), mmToPt(25f))
                        .hAlign(HAlign.LEFT)
                        .build())
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .rowspan(3)
                .padding(2f)
                .paddingRight(mmToPt(6f))
                .build());

        topTable.add(pdf.newCell()
                .content(u.Name, unitNameFont)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .colspan(3)
                .backgroundColor(fd.getColor())
                .padding(2f)
                .minimumHeight(mmToPt(10f) + 4f)
                .build());

        topTable.add(pdf.newCell()
                .content(unitFont, "%d pts", u.Points)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .padding(2f)
                .build());

        if (u.TransportOptions != null && u.TransportOptions.length > 0) {
            TableBuilder transportTable = pdf.newTable()
                    .numColumns(2 * u.TransportOptions.length)
                    .totalWidth(mmToPt(20f * (float) u.TransportOptions.length))
                    .keepTogether(true)
                    .hAlign(HAlign.RIGHT)
                    .relativeWidths(3f, 2f);
            for (String to : u.TransportOptions) {
                String transportIconName = fd.getImage(to.charAt(0));
                File transportIconFile = downloader.download(Resource.TRANSPORT_ICONS.getURL(transportIconName), transportIconName);
                transportTable
                        .add(pdf.newCell()
                                .content(pdf.newImage()
                                        .source(transportIconFile)
                                        .scaleToFit(mmToPt(10f), mmToPt(10f))
                                        .build())
                                .hAlign(HAlign.RIGHT)
                                .vAlign(VAlign.MIDDLE)
                                .paddingLeft(2f)
                                .build())
                        .add(pdf.newCell()
                                .content(to.substring(1), transportFont)
                                .hAlign(HAlign.LEFT)
                                .vAlign(VAlign.TOP)
                                .paddingLeft(2f)
                                .build());

            }
            topTable
                    .add(pdf.newCell()
                            .content(transportTable.build())
                            .hAlign(HAlign.RIGHT)
                            .vAlign(VAlign.MIDDLE)
                            .colspan(2)
                            .padding(2f)
                            .build());
        } else {
            topTable
                    .add(pdf.newCell()
                            .content("", transportFont)
                            .hAlign(HAlign.RIGHT)
                            .vAlign(VAlign.MIDDLE)
                            .colspan(2)
                            .padding(2f)
                            .build());
        }

        titleCell(pdf, topTable, "Move");
        titleCell(pdf, topTable, "Counter", "Measures");
        titleCell(pdf, topTable, "Armour");
        titleCell(pdf, topTable, "Damage", "Points");
        titleCell(pdf, topTable, "Type");
        titleCell(pdf, topTable, "Special");

        dataCell(pdf, topTable, u.getMove());
        dataCell(pdf, topTable, u.getCounterMeasures());
        dataCell(pdf, topTable, u.getArmour());
        dataCell(pdf, topTable, u.getDamagePoints());
        dataCell(pdf, topTable, u.Type);
        dataCell(pdf, topTable, u.getSpecial());

        block.add(topTable.build());

        block.add(pdf.newPara()
                .add(u.getSquadSize(), squadSizeFont)
                .build());

        if (u.Weapons != null && u.Weapons.length > 0) {
            TableBuilder weaponsTable = pdf.newTable()
                    .numColumns(9)
                    .keepTogether(true)
                    .relativeWidths(23f, 9f, 11f, 9f, 9f, 9f, 9f, 9f, 37.5f);

            weaponsTable.add(pdf.newCell().content("", unitFont).build());
            titleCell(pdf, weaponsTable, "M&F");
            titleCell(pdf, weaponsTable, "Arc");
            titleCell(pdf, weaponsTable, "R(F)");
            titleCell(pdf, weaponsTable, "R(C)");
            titleCell(pdf, weaponsTable, "Shots");
            titleCell(pdf, weaponsTable, "Acc");
            titleCell(pdf, weaponsTable, "E");
            titleCell(pdf, weaponsTable, "Special");

            for (Weapon w : u.Weapons) {
                if (w.Shots == 0) {
                    continue;
                }

                dataCell(pdf, weaponsTable, w.Name);
                dataCell(pdf, weaponsTable, w.MoveFire);
                dataCell(pdf, weaponsTable, w.Arc);
                dataCell(pdf, weaponsTable, w.RangeFull);
                dataCell(pdf, weaponsTable, w.RangeCountered);
                dataCell(pdf, weaponsTable, w.getShots());
                dataCell(pdf, weaponsTable, w.getAccuracy());
                dataCell(pdf, weaponsTable, w.getEnergy());
                dataCell(pdf, weaponsTable, w.getSpecial());
            }

            block.add(weaponsTable.build());
        }

        if (u.ExtraRules != null && u.ExtraRules.length > 0) {
            for (String er : u.ExtraRules) {
                String title = null;
                String rule = er;
                int index = er.indexOf(":");
                if (index != -1) {
                    title = er.substring(0, index).trim();
                    rule = er.substring(index + 1).trim();
                    try {
                        Integer.parseInt(title);
                        title = null;
                        rule = er;
                    } catch (NumberFormatException ex) {

                    }
                }
                if (title != null) {
                    block.add(pdf.newPara()
                            .add(title, ruleTitleFont)
                            .build());
                }
                block.add(pdf.newPara()
                        .add(rule, ruleFont)
                        .hAlign(HAlign.JUSTIFIED)
                        .build());
            }
        }

        block.buildAndAdd();
    }

    private void titleCell(PdfMaker pdf, TableBuilder table, String... text) {
        CellBuilder cell = pdf.newCell()
                .backgroundColor(Color.DARK_GRAY)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .padding(2f);
        if (text.length > 1) {
            ParaBuilder para = pdf.newPara();
            for (int i = 0; i < text.length; i++) {
                para.add(text[i], unitLabelFont);
                if (i + 1 < text.length) {
                    para.newLine();
                }
            }
            cell.content(para.build());
        } else {
            cell.content(text[0], unitLabelFont);
        }
        table.add(cell.build());
    }

    private void dataCell(PdfMaker pdf, TableBuilder table, String text) {
        table.add(pdf.newCell()
                .content(text, unitFont)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .padding(2f)
                .build());
    }

    private ArrayList<Unit> getUnits(Unit[] units, String... category) {
        HashSet<String> categories = new HashSet<>();
        for (String c : category) {
            categories.add(c);
        }
        ArrayList<Unit> list = new ArrayList<>();
        for (Unit u : units) {
            if (categories.contains(u.Category)) {
                list.add(u);
            }
        }
        list.sort(this::compareUnits);
        return list;
    }

    private int compareUnits(Unit u1, Unit u2) {
        return u1.Name.compareTo(u2.Name);
    }

    public static void main(String[] args) {
        try {
            new DropPDF().run();
        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace(System.err);
        }
    }

}
