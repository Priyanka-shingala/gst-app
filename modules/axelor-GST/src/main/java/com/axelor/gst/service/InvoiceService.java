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

import java.util.Date;

import com.axelor.gst.db.Invoice;
import com.axelor.party.db.Address;

public interface InvoiceService {

	public Date setFromDate();

	public Address addShippingAddress(Address invoiceAddress, Address shippingAddress);

	public Invoice setStatusValidate(Invoice invoice);

	public Invoice setStatusPaid(Invoice invoice);

	public Invoice setStatusCancelled(Invoice invoice);

}
