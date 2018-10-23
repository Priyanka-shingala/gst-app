/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2018 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.gst.web;

import javax.inject.Inject;

import com.axelor.company.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class SequenceController {

	@Inject
	private SequenceService compService;

	/*
	 * This method is generate sequence based on padding.
	 */
	public void nextNoGenerator(ActionRequest req, ActionResponse res) {

		Sequence seq = req.getContext().asType(Sequence.class);
		StringBuilder fcn = new StringBuilder();
		fcn = compService.nextNoGenerator(seq);
		res.setValue("nextNumber", fcn.toString());
	}
}
