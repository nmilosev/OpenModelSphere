/*************************************************************************

Copyright (C) 2008 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.be.preview;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.PropertyScreenPreviewInfo;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.db.srtypes.BETimeUnit;
import org.modelsphere.sms.be.international.LocaleMgr;

public class BPMPreview extends JackForwardEngineeringPlugin {
    private static final String TIME_ALLOCATION = LocaleMgr.misc.getString("TimeAllocation");
    private static final String RESOURCE_WORKLOAD = LocaleMgr.misc.getString("ResourceWorkload");
    private static final String RESOURCE_VALUES = LocaleMgr.misc.getString("ResourceValues");
    private static final String FIXED_VALUES = LocaleMgr.misc.getString("FixedValues");
    private static final String PARTIAL_VALUES = LocaleMgr.misc.getString("PartialValues");
    private static final String TOTAL_VALUES = LocaleMgr.misc.getString("TotalValues");
    private static final String UNNAMED_ROLE = LocaleMgr.misc.getString("UnnamedRole");
    private static final String NONE = LocaleMgr.misc.getString("none");
    private static final String UNSPECIFIED = LocaleMgr.misc.getString("unspecified");
    private static String COST;
    private static String TIME;
    private static String USAGE_COST;

    private static final int HIDDEN_PLUGIN = -1;
    //not an actual plug-in, but needs signature
    private PluginSignature g_signature = new PluginSignature(BPMPreview.class.getName(),
            "$Revision: 10 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            HIDDEN_PLUGIN, true);

    private static boolean g_init = false;

    public BPMPreview() {
        if (!g_init) {
            COST = DbBEUseCase.fResourceCost.getGUIName();
            TIME = DbBEUseCase.fResourceTime.getGUIName();
            USAGE_COST = DbBEUseCase.fTotalCost.getGUIName();
            g_init = true;
        }
    } //end BPMPreview()

    ////////////////////
    // OVERRIDES Plugin
    public PluginSignature getSignature() {
        return g_signature;
    }

    //
    /////////////////////

    ////////////////////
    // OVERRIDES Forward
    private PropertyScreenPreviewInfo m_propertyScreenPreviewInfo = null;

    public PropertyScreenPreviewInfo getPropertyScreenPreviewInfo() {
        if (m_propertyScreenPreviewInfo == null) {
            m_propertyScreenPreviewInfo = new PropertyScreenPreviewInfo() {
                public String getTabName() {
                    return TIME_ALLOCATION;
                }

                public String getContentType() {
                    return "text/html";
                } //NOT LOCALIZABLE, content type

                public Class[] getSupportedClasses() {
                    return new Class[] {}; //{ DbBEUseCase.class }; UNCOMMENT IT TO ENABLE THIS FEATURE
                }
            };
        }

        return m_propertyScreenPreviewInfo;
    } //end getPropertyScreenPreviewInfo()

    public Class[] getSupportedClasses() {
        return new Class[] {};
    }

    protected void forwardTo(DbObject semObj, ArrayList generatedFiles) throws DbException,
            IOException, RuleException {
        String name = semObj.getName();

        // has to be coded
    } //end forwardTo()

    public void setOutputToASCIIFormat() {
    }

    private BPMPreviewRule m_rule = new BPMPreviewRule();

    public Rule getRuleOf(DbObject so, int context) {
        return m_rule;
    } //end getRuleOf()

    //
    ///////////////////

    //
    // INNER CLASSES
    //
    private class BPMPreviewRule extends Rule {

        //Note : for private inner classes, visibility of the default constructor is private
        //So, it is required to declare an empty constructor with package visibility
        BPMPreviewRule() {
        }

        public boolean expand(Writer writer, Serializable obj, RuleOptions options)
                throws IOException {
            try {
                if (obj instanceof DbBEUseCase) {
                    DbBEUseCase process = (DbBEUseCase) obj;
                    process(writer, process);
                } //end if
            } catch (DbException ex) {
                writer.write(ex.toString());
            } //end try

            return true;
        } //end expand()

        private void process(Writer writer, DbBEUseCase process) throws IOException, DbException {
            //fill the process list
            ArrayList processList = new ArrayList();
            ArrayList resourceList = new ArrayList();
            DbRelationN relN = process.getComponents();
            DbEnumeration dbEnum = relN.elements(DbBEUseCase.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEUseCase subProcess = (DbBEUseCase) dbEnum.nextElement();
                processList.add(subProcess);
            } //end while
            dbEnum.close();

            //fill the resource list
            DbBEModel model = (DbBEModel) process.getCompositeOfType(DbBEModel.metaClass);
            relN = model.getComponents();
            dbEnum = relN.elements(DbBEResource.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbBEResource resource = (DbBEResource) dbEnum.nextElement();
                resourceList.add(resource);
            } //end while
            dbEnum.close();

            StringWriter buffer = new StringWriter();
            PrintWriter printer = new PrintWriter(buffer);
            fillBuffer(printer, process, processList, resourceList);
            String s = buffer.toString();
            writer.write(s);
        } //end process()

        private void fillBuffer(PrintWriter writer, DbBEUseCase process, ArrayList processList,
                ArrayList resourceList) throws DbException {

            int nbColumns = processList.size();
            writer.println("<table BORDER COLS=" + (3 + nbColumns) + " WIDTH=\"100%\" > ");

            //write the header
            writer.println("<tr>");
            writer.println("<td WIDTH=\"150\"></td>");

            for (int i = 0; i < nbColumns; i++) {
                DbBEUseCase subProcess = (DbBEUseCase) processList.get(i);
                String ID = subProcess.getAlphanumericHierID();
                String name = subProcess.getName();
                String s = "<td WIDTH=\"150\"><b>" + ID + "(" + name + ")" + "</b></td>";
                writer.println(s);
            } //end for

            String ID = process.getAlphanumericHierID();
            String name = process.getName();
            writer.println("<td WIDTH=\"150\"><b>" + ID + "(" + name + ")" + "</b></td>");

            writer.println("<td WIDTH=\"150\"><b>" + RESOURCE_WORKLOAD + "</b></td>");
            writer.println("</tr>");

            //write the content
            int nbRows = resourceList.size();
            ArrayList resourceProcessList = new ArrayList();
            ArrayList resourceProcessNodeList = new ArrayList();
            for (int i = 0; i < nbRows; i++) {
                writer.println("<tr>");
                DbBEResource resource = (DbBEResource) resourceList.get(i);
                String resName = resource.getName();
                writer.println("<td WIDTH=\"150\">");
                String s = "<b>" + resName + "</b><br>";
                writer.println(s);

                Double cost = resource.getCost();
                String costStr = NONE;
                if (cost != null) {
                    costStr = cost.toString();
                    BETimeUnit unit = resource.getCostTimeUnit();
                    if (unit != null) {
                        costStr += " / " + unit.toString();
                    }
                } //end if

                writer.println(COST + " = " + costStr);
                writer.println("</td>");

                DbRelationN relN = resource.getProcessUsages();
                DbEnumeration dbEnum = relN.elements(DbBEUseCaseResource.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEUseCaseResource resourceProcess = (DbBEUseCaseResource) dbEnum
                            .nextElement();
                    resourceProcessNodeList.add(resourceProcess);

                    DbBEUseCase proc = (DbBEUseCase) resourceProcess
                            .getCompositeOfType(DbBEUseCase.metaClass);
                    resourceProcessList.add(proc);
                } //end while
                dbEnum.close();

                for (int j = 0; j < nbColumns; j++) {
                    DbBEUseCase subProcess = (DbBEUseCase) processList.get(j);

                    writer.println("<td WIDTH=\"150\">");
                    if (resourceProcessList.contains(subProcess)) {
                        int idx = resourceProcessList.indexOf(subProcess);
                        DbBEUseCaseResource usage = (DbBEUseCaseResource) resourceProcessNodeList
                                .get(idx);

                        Double time = usage.getUsageRate();
                        double value = (time == null) ? 0.0 : time.doubleValue();
                        BETimeUnit unit = usage.getUsageRateTimeUnit();
                        String msg = TIME + " = " + getFormattedTime(value, unit);
                        writer.println(msg + "<br>");

                        Double costVal = computeUsageCost(resource, usage);
                        writer.println(USAGE_COST + " = " + costVal);
                    } //end if
                    writer.println("</td>");
                } //end for

                //write main process
                String text = "";
                relN = resource.getProcessUsages();
                dbEnum = relN.elements(DbBEUseCaseResource.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEUseCaseResource node = (DbBEUseCaseResource) dbEnum.nextElement();
                    DbBEUseCase proc = (DbBEUseCase) node.getCompositeOfType(DbBEUseCase.metaClass);
                    if (proc.equals(process)) {
                        String role = node.getRole();
                        role = (role == null) ? UNNAMED_ROLE : role;
                        Double time = node.getUsageRate();
                        double value = (time == null) ? 0.0 : time.doubleValue();
                        BETimeUnit unit = node.getUsageRateTimeUnit();
                        text = role + " " + getFormattedTime(value, unit);
                        break;
                    } //end if
                } //end while
                dbEnum.close();

                String s2 = "<td WIDTH=\"150\">" + text + "</td>";
                writer.println(s2);

                //write resource work load
                Double time = resource.getWorkLoad();
                double value = (time == null) ? 0.0 : time.doubleValue();
                BETimeUnit unit = resource.getWorkLoadTimeUnit();
                text = getFormattedTime(value, unit);
                s2 = "<td WIDTH=\"150\">" + text + "</td>";
                writer.println(s2);
                writer.println("</tr>");

                //clear for the next resource
                resourceProcessList.clear();
                resourceProcessNodeList.clear();
            } //end for

            //Write line trailer : Resource Time
            writer.println("<tr>");
            writer.println("<td WIDTH=\"150\"><b>" + RESOURCE_VALUES + "</b></td>");

            for (int i = 0; i < nbColumns; i++) {
                DbBEUseCase subProcess = (DbBEUseCase) processList.get(i);
                Double time = subProcess.getResourceTime();
                BETimeUnit unit = subProcess.getResourceTimeUnit();
                Double cost = subProcess.getResourceCost();
                printValues(writer, time, unit, cost);
            } //end for

            Double time = process.getResourceTime();
            double value = (time == null) ? 0.0 : time.doubleValue();
            BETimeUnit unit = process.getResourceTimeUnit();
            String text = getFormattedTime(value, unit);
            String s2 = "<td WIDTH=\"150\">" + text + "</td>";
            writer.println(s2);
            writer.println("</tr>");

            //Write line trailer : Fixed Time
            writer.println("<tr>");
            writer.println("<td WIDTH=\"150\"><b>" + FIXED_VALUES + "</b></td>");

            for (int i = 0; i < nbColumns; i++) {
                DbBEUseCase subProcess = (DbBEUseCase) processList.get(i);
                time = subProcess.getFixedTime();
                unit = subProcess.getFixedTimeUnit();
                Double cost = subProcess.getFixedCost();
                printValues(writer, time, unit, cost);
            } //end for

            time = process.getFixedTime();
            value = (time == null) ? 0.0 : time.doubleValue();
            unit = process.getFixedTimeUnit();
            text = getFormattedTime(value, unit);
            s2 = "<td WIDTH=\"150\">" + text + "</td>";
            writer.println(s2);

            //s2 = "<td WIDTH=\"150\">" + "</td>";
            //writer.println(s2);
            writer.println("</tr>");

            //Write line trailer : Partial Time
            writer.println("<tr>");
            writer.println("<td WIDTH=\"150\"><b>" + PARTIAL_VALUES + "</b></td>");

            for (int i = 0; i < nbColumns; i++) {
                DbBEUseCase subProcess = (DbBEUseCase) processList.get(i);
                time = subProcess.getPartialTime();
                unit = subProcess.getPartialTimeUnit();
                Double cost = subProcess.getPartialCost();
                printValues(writer, time, unit, cost);
            } //end for

            time = process.getPartialTime();
            value = (time == null) ? 0.0 : time.doubleValue();
            unit = process.getPartialTimeUnit();
            text = getFormattedTime(value, unit);
            s2 = "<td WIDTH=\"150\">" + text + "</td>";
            writer.println(s2);

            //s2 = "<td WIDTH=\"150\">" + "</td>";
            //writer.println(s2);
            writer.println("</tr>");

            //Write line trailer : Total Time
            writer.println("<tr>");
            writer.println("<td WIDTH=\"150\"><b> " + TOTAL_VALUES + "</b></td>");

            for (int i = 0; i < nbColumns; i++) {
                DbBEUseCase subProcess = (DbBEUseCase) processList.get(i);
                time = subProcess.getTotalTime();
                unit = subProcess.getTotalTimeUnit();
                Double cost = subProcess.getTotalCost();
                printValues(writer, time, unit, cost);
            } //end for

            time = process.getTotalTime();
            value = (time == null) ? 0.0 : time.doubleValue();
            unit = process.getTotalTimeUnit();
            text = getFormattedTime(value, unit);
            s2 = "<td WIDTH=\"150\">" + text + "</td>";
            writer.println(s2);

            //s2 = "<td WIDTH=\"150\">" + "</td>";
            //writer.println(s2);
            writer.println("</tr>");

            writer.println("</table>");
        } //end fillBuffer()

        private void printValues(PrintWriter writer, Double time, BETimeUnit unit, Double cost)
                throws DbException {
            writer.println("<td WIDTH=\"150\">");
            String text = NONE;
            if (time != null) {
                double value = (time == null) ? 0.0 : time.doubleValue();
                text = getFormattedTime(value, unit);
            }
            writer.println(TIME + " = " + text + "<br>");

            text = NONE;
            if (cost != null) {
                text = cost.toString();
            }
            writer.println(COST + " = " + text);

            writer.println("</td>");
        } //end printValues()

        private Double computeUsageCost(DbBEResource resource, DbBEUseCaseResource usage)
                throws DbException {
            Double usageCost;

            Double resCost = resource.getCost();
            BETimeUnit resUnit = resource.getCostTimeUnit();
            int rsrcCostUnit = (resUnit == null) ? 0 : resUnit.getValue();
            double pureCost = (resCost == null) ? 0.0 : resCost.doubleValue();

            Double rate = usage.getUsageRate();
            BETimeUnit rateUnit = usage.getUsageRateTimeUnit();
            int rscrUsageRateUnit = (rateUnit == null) ? 0 : rateUnit.getValue();
            double rateVal = (rate == null) ? 0.0 : rate.doubleValue();

            boolean isRsrcFixedCost = (rsrcCostUnit == 0) && (rscrUsageRateUnit != 0);
            boolean isIntraLinkUnitValid = (rsrcCostUnit == 0) || (rscrUsageRateUnit != 0);

            if (!isIntraLinkUnitValid) {
                return new Double(Double.NaN);
            } //end if

            if (resCost == null) {
                return null;
            }

            if (isRsrcFixedCost) {
                usageCost = resCost;
            } else {
                if (resUnit == null) {
                    double nbDays = rateVal; //rateVal should be interpreted as number of days
                    double totalCost = pureCost * nbDays; //pureCost should be interpreted as cost per day
                    usageCost = new Double(totalCost);
                } else {

                    //get cost per second
                    double costPerSecond = pureCost;
                    if (rsrcCostUnit == BETimeUnit.HOUR) {
                        costPerSecond /= 3600.0;
                    } else if (rsrcCostUnit == BETimeUnit.MINUTE) {
                        costPerSecond /= 60.0;
                    } //end if

                    //get nb of seconds
                    double nbSecs = rateVal;
                    if (rscrUsageRateUnit == BETimeUnit.HOUR) {
                        nbSecs *= 3600.0;
                    } else if (rscrUsageRateUnit == BETimeUnit.MINUTE) {
                        nbSecs *= 60.0;
                    } //end if

                    usageCost = new Double(costPerSecond * nbSecs);
                } //end if
            } //end if

            return usageCost;
        } //end computeCost()

        private String getFormattedTime(double value, BETimeUnit unit) throws DbException {
            if (unit == null) {
                if (value == 0.0)
                    return "";
                else
                    return Double.toString(value) + " " + UNSPECIFIED;
            } else {
                double baseTime = getBaseTime(value, unit.getValue());
                TimeAmountFormat timeFormat = TimeAmountFormat.getHhMmSsFormat();
                String str = timeFormat.format((int) (baseTime * 1000));
                return str;
            }
        } //end getQualifiedName()

        private double getBaseTime(double time, int timeUnit) {
            switch (timeUnit) {
            case BETimeUnit.MINUTE:
                return (time * 60.0); //60.0 for floating-point division
            case BETimeUnit.HOUR:
                return (time * 3600.0); //3600.0 for floating-point division
                // NA or BETimeUnit.SECOND
            default:
                return time;
            }
        } //end getBaseTime()
    } //end BPMPreviewRule

    /*
     * Possible formats : 8 hr 7 min 08 hr 07 min 08:07:07 195 secs ...
     */
    private static class TimeAmountFormat {
        private static final String DEC_PATTERN = "00"; //at least 2 digits
        private static final String DEFAULT_PATTERN = "{0} {1} {2} {3}"; //default pattern : hr min sec ms
        private static final String[] DEFAULT_PATTERNS = new String[] { "{0} hr ", "{0} min ",
                "{0} sec ", "{0} ms " }; //NOT LOCALIZABLE, already international!

        // Constants used internally; unit is milliseconds
        private static final int ONE_SECOND = 1000;
        private static final int ONE_MINUTE = 60 * ONE_SECOND;
        private static final int ONE_HOUR = 60 * ONE_MINUTE;

        private NumberFormat m_hourFormat;
        private NumberFormat m_minuFormat;
        private NumberFormat m_secoFormat;
        private NumberFormat m_millFormat;
        private String m_pattern;
        private String[] m_patterns;

        //main constructor
        //let public visibility for RetroGuard
        public TimeAmountFormat(String pattern, String[] patterns, NumberFormat[] numberFormats) {
            m_pattern = pattern;
            m_patterns = patterns;
            m_hourFormat = numberFormats[0];
            m_minuFormat = numberFormats[1];
            m_secoFormat = numberFormats[2];
            m_millFormat = numberFormats[3];
        } //end TimeAmountFormat()

        //let public visibility for RetroGuard
        public TimeAmountFormat() {
            this(DEFAULT_PATTERN, DEFAULT_PATTERNS, new NumberFormat[] { null, null,
                    new DecimalFormat(DEC_PATTERN), null });
        }

        private static TimeAmountFormat g_defFormat = null;

        public static TimeAmountFormat getDefaultFormat() {
            if (g_defFormat == null) {
                g_defFormat = new TimeAmountFormat();
            }

            return g_defFormat;
        } //end getDefaultFormat()

        private static TimeAmountFormat g_hhMmSsFormat = null;

        public static TimeAmountFormat getHhMmSsFormat() {
            if (g_hhMmSsFormat == null) {
                NumberFormat[] numberFormats = new NumberFormat[] { new DecimalFormat(DEC_PATTERN),
                        new DecimalFormat(DEC_PATTERN), new DecimalFormat(DEC_PATTERN),
                        new DecimalFormat(DEC_PATTERN) };
                g_hhMmSsFormat = new TimeAmountFormat(DEFAULT_PATTERN, DEFAULT_PATTERNS,
                        numberFormats);
            }

            return g_hhMmSsFormat;
        } //end getDefaultFormat()

        public String format(int milliseconds) {
            String hr = "", min = "", sec = "", ms = "";

            if ((m_hourFormat != null) && (milliseconds >= ONE_HOUR)) {
                hr = MessageFormat.format(m_patterns[0], new Object[] { m_hourFormat
                        .format(milliseconds / ONE_HOUR) });
                milliseconds = milliseconds % ONE_HOUR;
            } //end if

            if ((m_minuFormat != null) && (milliseconds >= ONE_MINUTE)) {
                min = MessageFormat.format(m_patterns[1], new Object[] { m_minuFormat
                        .format(milliseconds / ONE_MINUTE) });
                milliseconds = milliseconds % ONE_MINUTE;
            } //end if

            if ((m_secoFormat != null) && (milliseconds >= ONE_SECOND)) {
                sec = MessageFormat.format(m_patterns[2], new Object[] { m_secoFormat
                        .format(milliseconds / ONE_SECOND) });
                milliseconds = milliseconds % ONE_SECOND;
            } //end if

            if (m_millFormat != null) {
                if (milliseconds > 0) {
                    ms = MessageFormat.format(m_patterns[3], new Object[] { m_hourFormat
                            .format(milliseconds) });
                }
            } //end if

            String text = MessageFormat.format(m_pattern, new Object[] { hr, min, sec, ms });
            return text;
        } //end format
    } //end TimeAmountFormat

} //end BPMPreview

