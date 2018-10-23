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

import com.axelor.company.db.Sequence;
import com.axelor.company.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;

public class PartyController {

	/*
	 * This method is set reference field which is Get from sequence on save.
	 */
	@Transactional
	public void refPartyGenrator(ActionRequest req, ActionResponse res) {

		Sequence seq = Beans.get(SequenceRepository.class).all().filter("self.model.name = 'Party'").fetchOne();

		if (seq == null) {
			String message = String.format("No sequence for Party, Please set sequence for this model");
			res.setFlash(message);
		} else {
			String nextno = seq.getNextNumber();
			nextno = nextno.substring(2, nextno.length() - 2);
			int result11 = Integer.parseInt(nextno);
			result11 = result11 + 1;
			nextno = Integer.toString(result11);
			StringBuilder fn = new StringBuilder();
			Integer padd = seq.getPadding();
			if (seq.getPrefix() != null) {
				fn.append(seq.getPrefix());
			}

			for (int i = 0; i < padd - nextno.length(); i++) {
				fn.append("0");
			}
			fn.append(nextno);
			if (seq.getSuffix() != null) {
				fn.append(seq.getSuffix());
			}

			String nextParty = fn.toString();
			seq.setNextNumber(nextParty);
			Beans.get(SequenceRepository.class).save(seq);
			res.setValue("reference", nextParty);
		}
	}
}
