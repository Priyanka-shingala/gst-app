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
package com.axelor.gst.service;

import com.axelor.company.db.Sequence;

public class SequenceServiceImpl implements SequenceService {

	@Override
	public StringBuilder nextNoGenerator(Sequence seq) {

		StringBuilder fn = new StringBuilder();

		if (seq.getPrefix() != null) {
			fn.append(seq.getPrefix());
		}

		for (int i = 0; i < seq.getPadding(); i++) {
			fn.append("0");
		}

		if (seq.getSuffix() != null) {
			fn.append(seq.getSuffix());
		}

		return fn;

	}

}
