/*
 * Copyright (C) 2020 Dominik Derwiński
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import pl.derwinski.download.DownloadMode;
import pl.derwinski.download.Downloader;
import pl.derwinski.droppdf.json.Faction;
import pl.derwinski.droppdf.json.LaunchAsset;
import pl.derwinski.droppdf.json.Ship;
import pl.derwinski.droppdf.json.ShipWeapon;
import pl.derwinski.droppdf.json.Transport;
import pl.derwinski.droppdf.json.Unit;
import pl.derwinski.droppdf.json.Weapon;
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

    private static final DownloadMode DOWNLOAD_MODE = DownloadMode.JSON;

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
    private Font shipNameFont;
    private Font shipDesignationFont;
    private Font shipPtsFont;
    private Font unitNameFont;
    private Font unitLabelFont;
    private Font unitFont;
    private Font transportFont;
    private Font squadSizeFont;
    private Font ruleTitleFont;
    private Font ruleFont;

    public DropPDF() throws Exception {
        downloader = new Downloader(new File("."), DOWNLOAD_MODE);
        mapper = JsonMapper.builder()
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .build();
    }

    public void run() throws Exception {
        Faction[] factions = mapper.readValue(downloader.download(Resource.FACTIONS.getURL(), "factions.json"), Faction[].class);
        //FactionV2[] factionsv2 = mapper.readValue(downloader.download(Resource.FACTIONSV2.getURL(), "factionsv2.json"), FactionV2[].class);
        for (Faction f : factions) {
            fd = FactionData.valueOf(f.Name);
            try (PdfMaker pdf = new PdfMaker(downloader.getFile("DZC_%s_A4.pdf", f.Name), PageSize.A4)) {
                writeDZC(pdf, f);
            }
//            try (PdfMaker pdf = new PdfMaker(downloader.getFile("DZC_%s_Letter.pdf", f.Name), PageSize.LETTER)) {
//                writeDZC(pdf, f);
//            }
            try (PdfMaker pdf = new PdfMaker(downloader.getFile("DFC_%s_A4.pdf", f.Name), PageSize.A4)) {
                writeDFC(pdf, f);
            }
//            try (PdfMaker pdf = new PdfMaker(downloader.getFile("DFC_%s_Letter.pdf", f.Name), PageSize.LETTER)) {
//                writeDFC(pdf, f);
//            }
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
        unitLabelFont = pdf.getFont(ss, 8, Color.WHITE);
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
        writeUnits(pdf, f, "Auxiliary & Transports", units, "Auxiliary", "Airstrike", "Transport", "Gate", "Drill");
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

    private ArrayList<Unit> getUnits(Unit[] units, String... category) {
        HashSet<String> categories = new HashSet<>();
        for (String c : category) {
            categories.add(c.toUpperCase());
        }
        ArrayList<Unit> list = new ArrayList<>();
        for (Unit u : units) {
            if (categories.contains(u.category.toUpperCase().trim())) {
                list.add(u);
            }
        }
        list.sort(this::compareUnits);
        return list;
    }

    private int compareUnits(Unit u1, Unit u2) {
        return u1.name.compareTo(u2.name);
    }

    private void writeUnit(PdfMaker pdf, Faction f, Unit u) throws Exception {
        File unitPhotoFile = downloader.download(Resource.UNIT_PHOTOS.getURL(f.Name, u.name), "%s_%s.png", f.Name, u.name);

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
                .content(u.name, unitNameFont)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .colspan(3)
                .backgroundColor(fd.getColor())
                .padding(2f)
                .minimumHeight(mmToPt(10f) + 4f)
                .build());

        topTable.add(pdf.newCell()
                .content(unitFont, "%d pts", u.points)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .padding(2f)
                .build());

        if (u.transport != null && u.transport.length > 0) {
            TableBuilder transportTable = pdf.newTable()
                    .numColumns(2 * u.transport.length)
                    .totalWidth(mmToPt(20f * (float) u.transport.length))
                    .keepTogether(true)
                    .hAlign(HAlign.RIGHT)
                    .relativeWidths(3f, 2f);
            for (Transport to : u.transport) {
                String transportIconName = to.transportIcon.substring(to.transportIcon.lastIndexOf("/") + 1);
                File transportIconFile = downloader.download(Resource.TRANSPORT_ICONS.getURL(f.Name, transportIconName), String.format("%s_%s", f.Name, transportIconName));
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
                                .content(String.format("%d", to.number), transportFont)
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
        titleCell(pdf, topTable, "CM");
        titleCell(pdf, topTable, "Armour");
        titleCell(pdf, topTable, "Damage");
        titleCell(pdf, topTable, "Type");
        titleCell(pdf, topTable, "Special");

        dataCell(pdf, topTable, u.getMove());
        dataCell(pdf, topTable, u.countermeasures);
        dataCell(pdf, topTable, u.getArmour());
        dataCell(pdf, topTable, u.getDamagePoints());
        dataCell(pdf, topTable, u.type);
        dataCell(pdf, topTable, formatSpecial(u.special, "-", ", "));

        block.add(topTable.build());

        block.add(pdf.newPara()
                .add(u.getSquadSize(), squadSizeFont)
                .build());

        if (u.weapons != null && u.weapons.length > 0) {
            TableBuilder weaponsTable = pdf.newTable()
                    .numColumns(9)
                    .keepTogether(true)
                    .relativeWidths(23f, 9f, 11f, 9f, 9f, 9f, 9f, 9f, 37.5f);

            weaponsTable.add(pdf.newCell().content("", unitFont).build());
            titleCell(pdf, weaponsTable, "M&F");
            titleCell(pdf, weaponsTable, "ARC");
            titleCell(pdf, weaponsTable, "R(F)");
            titleCell(pdf, weaponsTable, "R(C)");
            titleCell(pdf, weaponsTable, "SHO");
            titleCell(pdf, weaponsTable, "ACC");
            titleCell(pdf, weaponsTable, "E");
            titleCell(pdf, weaponsTable, "Special");

            for (Weapon w : u.weapons) {
                dataCell(pdf, weaponsTable, w.name);
                dataCell(pdf, weaponsTable, w.mf == null ? "-" : w.mf);
                dataCell(pdf, weaponsTable, w.arc);
                dataCell(pdf, weaponsTable, w.rangeFull);
                dataCell(pdf, weaponsTable, w.rangeCountered);
                dataCell(pdf, weaponsTable, w.getShots());
                dataCell(pdf, weaponsTable, w.getAccuracy());
                dataCell(pdf, weaponsTable, w.getEnergy());
                dataCell(pdf, weaponsTable, formatSpecial(w.special, "-", ", "));
            }

            block.add(weaponsTable.build());
        }

        if (u.uniqueRules != null && u.uniqueRules.length > 0) {
            for (String er : u.uniqueRules) {
                String title = null;
                String rule = er;
                int index = er.indexOf(":");
                if (index != -1) {
                    title = er.substring(0, index).trim();
                    rule = er.substring(index + 1).trim();
                    try {
                        Integer.valueOf(title);
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

    private void writeDFC(PdfMaker pdf, Faction f) throws Exception {
        fd = FactionData.valueOf(f.Name);
        ss = pdf.getBaseFont("/Squarish Sans CT Regular SC.ttf");
        lsr = pdf.getBaseFont("/LiberationSerif-Regular.ttf");
        lsi = pdf.getBaseFont("/LiberationSerif-Italic.ttf");
        titleFont = pdf.getFont(ss, 60, Color.BLACK);
        loreFont = pdf.getFont(lsi, 12, Color.BLACK);
        gamePlayFont = pdf.getFont(lsr, 12, Color.BLACK);
        revFont = pdf.getFont(lsr, 8, Color.BLACK);
        categoryFont = pdf.getFont(ss, 14, fd.getTextColor());
        shipNameFont = pdf.getFont(ss, 14, Color.BLACK);
        shipDesignationFont = pdf.getFont(ss, 12, Color.BLACK);
        shipPtsFont = pdf.getFont(ss, 14, fd.getTextColor());
        unitLabelFont = pdf.getFont(ss, 8, Color.WHITE);
        unitFont = pdf.getFont(ss, 8, Color.BLACK);
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
                        .add(f.FleetGamePlay, gamePlayFont)
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

        Ship[] ships = mapper.readValue(Resource.SHIPS.fixJSON(downloader.download(Resource.SHIPS.getURL(f.Name), "%s_ships.json", f.Name)), Ship[].class);

        writeShips(pdf, f, "Super Heavy", ships, "S", "S2");
        writeShips(pdf, f, "Heavy", ships, "H");
        writeShips(pdf, f, "Medium", ships, "M");
        writeShips(pdf, f, "Light", ships, "L", "L2");
    }

    private void writeShips(PdfMaker pdf, Faction f, String title, Ship[] ships, String... tonnage) throws Exception {
        ArrayList<Ship> list = getShips(ships, tonnage);
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

        for (Ship s : list) {
            writeShip(pdf, f, s);
        }

        pdf.getDocument().newPage();
    }

    private ArrayList<Ship> getShips(Ship[] ships, String... tonnage) {
        HashSet<String> tonnages = new HashSet<>();
        for (String t : tonnage) {
            tonnages.add(t.toUpperCase());
        }
        ArrayList<Ship> list = new ArrayList<>();
        for (Ship s : ships) {
            if (tonnages.contains(s.Tonnage.toUpperCase().trim())) {
                list.add(s);
            }
        }
        list.sort(this::compareShips);
        return list;
    }

    private int compareShips(Ship s1, Ship s2) {
        int result = -Integer.compare(s1.TonnageClass, s2.TonnageClass);
        if (result == 0) {
            result = s1.Name.compareTo(s2.Name);
        }
        return result;
    }

    private void writeShip(PdfMaker pdf, Faction f, Ship s) throws Exception {
        File shipPhotoFile = downloader.download(Resource.SHIP_PHOTOS.getURL(f.Name, s.Name), "%s_%s.png", f.Name, s.Name);

        BlockBuilder block = pdf.newBlock()
                .paddingTop(mmToPt(2f))
                .paddingBottom(mmToPt(2f))
                .keepTogether(true)
                .spacingAfter(mmToPt(10f));

        TableBuilder nameTable = pdf.newTable()
                .numColumns(3)
                .keepTogether(true)
                .relativeWidths(4.5f, 8.5f, 4.5f);

        nameTable.add(pdf.newCell()
                .content(String.format("%s %s", s.Faction, s.Name), shipNameFont)
                .colspan(3)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOTTOM)
                .padding(mmToPt(2f))
                .build());

        nameTable.add(pdf.newCell()
                .content("", shipDesignationFont)
                .vAlign(VAlign.MIDDLE)
                .border(Border.TOP)
                .padding(mmToPt(2f))
                .build());

        nameTable.add(pdf.newCell()
                .content(s.Designation == null ? "" : s.Designation, shipDesignationFont)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.TOP)
                .padding(mmToPt(2f))
                .build());

        nameTable.add(pdf.newCell()
                .content(String.format("%d pts", s.Points), shipPtsFont)
                .backgroundColor(fd.getColor())
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .padding(mmToPt(2f))
                .build());

        block.add(nameTable.build());

        block.add(pdf.newImage()
                .source(shipPhotoFile)
                .scaleToFit(mmToPt(175f), mmToPt(60f))
                .hAlign(HAlign.CENTER)
                .build());

        TableBuilder statTable = pdf.newTable()
                .numColumns(10)
                .keepTogether(true)
                .relativeWidths(34f, 12.5f, 12.5f, 15f, 15.5f, 16.5f, 15f, 11f, 9f, 34f);

        titleCell(pdf, statTable, "Name");
        titleCell(pdf, statTable, "Scan");
        titleCell(pdf, statTable, "Sig");
        titleCell(pdf, statTable, "Thrust");
        titleCell(pdf, statTable, "Hull");
        titleCell(pdf, statTable, "Armour");
        titleCell(pdf, statTable, "PD");
        titleCell(pdf, statTable, "G");
        titleCell(pdf, statTable, "T");
        titleCell(pdf, statTable, "Special");

        dataCell(pdf, statTable, s.Name);
        dataCell(pdf, statTable, s.Scan);
        dataCell(pdf, statTable, s.Signal);
        dataCell(pdf, statTable, s.Thrust);
        dataCell(pdf, statTable, s.Hull);
        dataCell(pdf, statTable, s.Armour);
        dataCell(pdf, statTable, s.PointDefence);
        dataCell(pdf, statTable, s.getGroup());
        dataCell(pdf, statTable, s.Tonnage);
        dataCell(pdf, statTable, formatSpecial(s.Special, "-", ", "));

        block.add(statTable.build());

        if (s.Weapons != null && s.Weapons.length > 0) {
            TableBuilder weaponTable = pdf.newTable()
                    .numColumns(6)
                    .keepTogether(true)
                    .relativeWidths(56f, 15f, 15.5f, 16.5f, 15f, 57f);

            titleCell(pdf, weaponTable, "Type");
            titleCell(pdf, weaponTable, "Lock");
            titleCell(pdf, weaponTable, "Att");
            titleCell(pdf, weaponTable, "Dmg");
            titleCell(pdf, weaponTable, "Arc");
            titleCell(pdf, weaponTable, "Special");

            for (ShipWeapon w : s.Weapons) {
                dataCell(pdf, weaponTable, w.Name);
                dataCell(pdf, weaponTable, w.LockValue);
                dataCell(pdf, weaponTable, w.Attack);
                dataCell(pdf, weaponTable, w.Damage);
                dataCell(pdf, weaponTable, w.Arc);
                dataCell(pdf, weaponTable, formatSpecial(w.Special, "-", ", "));
            }

            block.add(weaponTable.build());
        }

        if (s.LaunchAssets != null && s.LaunchAssets.length > 0) {
            TableBuilder launchTable = pdf.newTable()
                    .totalWidth(0.6f * pdf.getContentWidth())
                    .numColumns(3)
                    .keepTogether(true)
                    .relativeWidths(56f, 23f, 24f);

            titleCell(pdf, launchTable, "Load");
            titleCell(pdf, launchTable, "Launch");
            titleCell(pdf, launchTable, "Special");

            for (LaunchAsset la : s.LaunchAssets) {
                dataCell(pdf, launchTable, la.Name);
                dataCell(pdf, launchTable, la.Launch);
                dataCell(pdf, launchTable, formatSpecial(la.Special, "-", ", "));
            }

            block.add(launchTable.build());
        }

        String hardPoints = s.getHardPoints();
        if (hardPoints != null) {
            block.add(pdf
                    .newTable()
                    .keepTogether(true)
                    .add(pdf
                            .newCell()
                            .content(pdf
                                    .newPara()
                                    .add("Hardpoints: ", ruleTitleFont)
                                    .add(hardPoints, ruleFont)
                                    .build())
                            .border(Border.BOX)
                            .padding(mmToPt(2f))
                            .build())
                    .build());
        }

        if (s.SpecRules != null && s.SpecRules.length > 0) {
            for (String er : s.SpecRules) {
                String title = null;
                String rule = er;
                int index = er.indexOf(":");
                if (index != -1) {
                    title = er.substring(0, index).trim();
                    rule = er.substring(index + 1).trim();
                    try {
                        Integer.valueOf(title);
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
                .content(text == null ? "" : text, unitFont)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE)
                .border(Border.BOX)
                .padding(2f)
                .build());
    }

    private String formatSpecial(String[] data, String none, String separator) {
        if (data == null || data.length == 0) {
            return none;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String d : data) {
                sb.append(d.trim());
                sb.append(separator);
            }
            sb.setLength(sb.length() - separator.length());
            return sb.toString();
        }
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
