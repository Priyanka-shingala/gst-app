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

import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.inject.Beans;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.product.db.Product;
import com.axelor.product.db.repo.ProductRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductServiceImpl implements ProductService {

	@Override
	public void invoiceBtn(ActionRequest request, ActionResponse response) {
		Product product = request.getContext().asType(Product.class);

		if ((request.getContext().get("_ids")) != null) {

			String tem = request.getContext().get("_ids").toString();
			tem = tem.substring(1, tem.length() - 1).replaceAll(" ", "");
			String[] ss = tem.split(",");

			List<InvoiceLine> liInvo = new ArrayList<InvoiceLine>();

			for (String string : ss) {
				product = Beans.get(ProductRepository.class).all().filter("self.id = (?)", string).fetchOne();

				InvoiceLine inVoi = new InvoiceLine();
				inVoi.setProduct(product);
				inVoi.setGstRate(product.getGstRate());
				inVoi.setItem(product.getCode() + ":" + product.getName());
				inVoi.setHSBN(product.getHSBN());
				inVoi.setPrice(product.getSalePrice());
				liInvo.add(inVoi);
			}
			response.setView(ActionView.define("Create Invoice").model(Invoice.class.getName())
					.add("form", "invoice-form").context("_invoiceItem", liInvo).map());
		} else {

			response.setView(ActionView.define("Create Invoice").model(Invoice.class.getName())
					.add("form", "invoice-form").map());
		}
	}
}
