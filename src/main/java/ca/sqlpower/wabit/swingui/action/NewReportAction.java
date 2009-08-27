/*
 * Copyright (c) 2008, SQL Power Group Inc.
 *
 * This file is part of Wabit.
 *
 * Wabit is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wabit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package ca.sqlpower.wabit.swingui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import ca.sqlpower.wabit.report.Report;
import ca.sqlpower.wabit.swingui.WabitIcons;
import ca.sqlpower.wabit.swingui.WabitSwingSession;

/**
 * An action that adds a new empty report to a particular workspace every time it
 * is invoked.
 */
public class NewReportAction extends AbstractAction {
	private final WabitSwingSession session;

    public NewReportAction(WabitSwingSession session) {
        super("New Report", WabitIcons.REPORT_ICON_16);
		this.session = session;
    }

    public void actionPerformed(ActionEvent e) {
        Report layout = new Report("New Report");
		session.getWorkspace().addReport(layout);
    }
}