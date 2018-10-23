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

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.axelor.app.AppSettings;
import com.axelor.company.db.Company;
import com.axelor.company.db.Sequence;
import com.axelor.company.db.repo.CompanyRepository;
import com.axelor.company.db.repo.SequenceRepository;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceService;
import com.axelor.inject.Beans;
import com.axelor.party.db.Address;
import com.axelor.party.db.Contact;
import com.axelor.party.db.Party;
import com.axelor.party.db.repo.ContactRepository;
import com.axelor.party.db.repo.PartyRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.persist.Transactional;

public class InvoiceController {

	@Inject
	private InvoiceService invoService;

	/*
	 * It will set Invoice and shipping address and contact from party.
	 */
	public void partyAddresses(ActionRequest req, ActionResponse res) {

		Invoice invoice = req.getContext().asType(Invoice.class);
		Party party = invoice.getParty();
		if (party != null) {
			List<Address> listAdd = party.getAddresses();
			Address address = new Address();
			for (Address address2 : listAdd) {
				if ((address2.getType().toString()).equals("invoice")
						|| (address2.getType().toString()).equals("default")) {
					address.setLine1(address2.getLine1());
					address.setLine2(address2.getLine2());
					address.setCity(address2.getCity());
					address.setState(address2.getState());
					address.setCountry(address2.getCountry());
					address.setPincode(address2.getPincode());
					address.setType(address2.getType());
					break;
				}
			}
			res.setValue("invoiceAddress", address);

			if (invoice.getUseInvoiceAddressAsShipping() == false) {
				Address shiAddress = new Address();
				for (Address address2 : listAdd) {
					if ((address2.getType().toString()).equals("shipping")
							|| (address2.getType().toString()).equals("default")) {
						shiAddress.setLine1(address2.getLine1());
						shiAddress.setLine2(address2.getLine2());
						shiAddress.setCity(address2.getCity());
						shiAddress.setState(address2.getState());
						shiAddress.setCountry(address2.getCountry());
						shiAddress.setPincode(address2.getPincode());
						shiAddress.setType(address2.getType());
						break;
					}
				}
				res.setValue("shippingAddress", shiAddress);
			}

			Contact contact = Beans.get(ContactRepository.class).all()
					.filter("self.type = 'primary' AND self in (?)", party.getContacts()).fetchOne();
			res.setValue("partyContact", contact);
		} else {
			String message = String.format("please select party first");
			res.setFlash(message);
		}
	}

	/*
	 * It will Calculate net Amount from invoice line.
	 */
	public void netCalculator(ActionRequest req, ActionResponse res) {

		Invoice invoice = req.getContext().asType(Invoice.class);
		List<InvoiceLine> invoiceItem = invoice.getInvoiceItem();
		BigDecimal amt = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceItem) {

			amt = amt.add(invoiceLine.getNetAmount());
		}
		BigDecimal netAmt = amt.divide(new BigDecimal(invoiceItem.size()));
		res.setValue("netAmount", netAmt);

		BigDecimal amtIgst = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceItem) {

			amtIgst = amtIgst.add(invoiceLine.getIGST());
		}
		BigDecimal netIgst = amtIgst.divide(new BigDecimal(invoiceItem.size()));
		res.setValue("netIGST", netIgst);

		BigDecimal amtCgst = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceItem) {

			amtCgst = amtCgst.add(invoiceLine.getCGST());
		}
		BigDecimal netCgst = amtCgst.divide(new BigDecimal(invoiceItem.size()));
		res.setValue("netCGST", netCgst);

		BigDecimal amtSgst = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceItem) {

			amtSgst = amtSgst.add(invoiceLine.getSGST());
		}
		BigDecimal netSgst = amtSgst.divide(new BigDecimal(invoiceItem.size()));
		res.setValue("netSGST", netSgst);

		BigDecimal amtGross = new BigDecimal(0);
		for (InvoiceLine invoiceLine : invoiceItem) {

			amtGross = amtGross.add(invoiceLine.getGrossAmt());
		}
		BigDecimal netGross = amtGross.divide(new BigDecimal(invoiceItem.size()));
		res.setValue("netSGST", netGross);
	}

	/*
	 * generate sequence from sequence object fill on validate state.
	 */
	@Transactional
	public void refGenrator(ActionRequest req, ActionResponse res) {

		Invoice invoice = req.getContext().asType(Invoice.class);
		String ref = invoice.getReference();
		if (ref == null) {
			Sequence seq = Beans.get(SequenceRepository.class).all().filter("self.model.name = 'Invoice'").fetchOne();
			if (seq == null) {
				String message = String.format("No sequence for Invoice, Please set sequence for this model");
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

				String nextInvoice = fn.toString();
				seq.setNextNumber(nextInvoice);
				Beans.get(SequenceRepository.class).save(seq);
				res.setValue("reference", nextInvoice);
			}
		} else {
			res.setValue("reference", ref);
		}
	}

	/*
	 * It will give 1st date of month for invoice chart.
	 */
	public void setFromDate(ActionRequest req, ActionResponse res) {
		Date firstDayOfTheMonth = invoService.setFromDate();
		res.setValue("fromDate", firstDayOfTheMonth);
	}

	public Address addShippingAddress(Address invoiceAddress, Address shippingAddress) {

		Address address = invoService.addShippingAddress(invoiceAddress, shippingAddress);
		return address;
	}

	/*
	 * This method is give a path for image which is used in report.
	 */
	public String addAttachmentPath() {

		String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");

		attachmentPath = attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;

		return attachmentPath;

	}

	/*
	 * Set default company in invoice.
	 */
	public Company defaultComapany() {

		Company company = Beans.get(CompanyRepository.class).all().fetchOne();
		return company;
	}

	public void setStatusValidate(ActionRequest req, ActionResponse res) {

		Invoice invoice = req.getContext().asType(Invoice.class);
		invoice = invoService.setStatusValidate(invoice);
		res.setValue("status", invoice.getStatus());
	}

	public void setStatusPaid(ActionRequest req, ActionResponse res) {

		Invoice invoice = req.getContext().asType(Invoice.class);
		invoice = invoService.setStatusPaid(invoice);
		res.setValue("status", invoice.getStatus());
	}

	public void setStatusCancelled(ActionRequest req, ActionResponse res) {

		Invoice invoice = req.getContext().asType(Invoice.class);
		invoice = invoService.setStatusCancelled(invoice);
		res.setValue("status", invoice.getStatus());
	}

	@Transactional
	public void partyAddressSave(Address invoiceAddress, Party party) {

		Party party1 = Beans.get(PartyRepository.class).all().filter("self.id = ?", party.getId()).fetchOne();
		List<Address> add = party1.getAddresses();
		for (Address address : add) {
			if (address.getType().equals(invoiceAddress.getType())) {
				address.setLine1(invoiceAddress.getLine1());
				address.setLine2(invoiceAddress.getLine2());
				address.setPincode(invoiceAddress.getPincode());
				address.setCity(invoiceAddress.getCity());
				address.setState(invoiceAddress.getState());
				address.setCountry(invoiceAddress.getCountry());
			}
		}
		party1.setAddresses(add);
		Beans.get(PartyRepository.class).save(party1);
	}
}
