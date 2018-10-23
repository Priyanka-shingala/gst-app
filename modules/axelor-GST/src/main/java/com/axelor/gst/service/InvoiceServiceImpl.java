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

import java.util.Calendar;
import java.util.Date;
import com.axelor.gst.db.Invoice;
import com.axelor.inject.Beans;
import com.axelor.party.db.Address;
import com.axelor.party.db.repo.AddressRepository;

public class InvoiceServiceImpl implements InvoiceService {

	@Override
	public Date setFromDate() {
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

		Date firstDayOfTheMonth = cal.getTime();
		return firstDayOfTheMonth;
	}

	@Override
	public Address addShippingAddress(Address invoiceAddress, Address shippingAddress) {

		shippingAddress = Beans.get(AddressRepository.class).copy(invoiceAddress, true);
		return shippingAddress;
	}

	@Override
	public Invoice setStatusValidate(Invoice invoice) {
		invoice.setStatus("validated");
		return invoice;
	}

	@Override
	public Invoice setStatusPaid(Invoice invoice) {
		invoice.setStatus("paid");
		return invoice;
	}

	@Override
	public Invoice setStatusCancelled(Invoice invoice) {
		invoice.setStatus("cancelled");
		return invoice;
	}

}
