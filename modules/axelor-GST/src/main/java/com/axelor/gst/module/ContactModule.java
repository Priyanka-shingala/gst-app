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
package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.InvoiceServiceImpl;
import com.axelor.gst.service.ProductService;
import com.axelor.gst.service.ProductServiceImpl;

public class ContactModule extends AxelorModule {

	@Override
	protected void configure() {
		bind(InvoiceService.class).to(InvoiceServiceImpl.class);
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(ProductService.class).to(ProductServiceImpl.class);
	}
}
