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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.axelor.app.AppSettings;
import com.axelor.gst.service.ProductService;
import com.axelor.inject.Beans;
import com.axelor.product.db.Product;
import com.axelor.product.db.repo.ProductRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {

	@Inject
	private ProductService prodService;

	/*
	 * It will open new invoice form with lines containing selected products.
	 */
	public void invoiceBtn(ActionRequest request, ActionResponse response) {

		prodService.invoiceBtn(request, response);
	}

	/*
	 * This method is give a path for image which is used in report.
	 */
	public String addAttachmentPath() {

		String attachmentPath = AppSettings.get().getPath("file.upload.dir", "");

		attachmentPath = attachmentPath.endsWith(File.separator) ? attachmentPath : attachmentPath + File.separator;

		return attachmentPath;

	}

	public void productReport(ActionRequest req, ActionResponse res) {
		if ((req.getContext().get("_ids")) != null) {
			String tem = req.getContext().get("_ids").toString();
			tem = tem.substring(1, tem.length() - 1).replaceAll(" ", "");
			req.getContext().put("_param", tem);
			return;
		}
		List<Product> productList = Beans.get(ProductRepository.class).all().fetch();
		List<Long> longList = new ArrayList<>();
		for (Product product2 : productList) {
			Long lonId = product2.getId();
			longList.add(lonId);
		}
		String str = StringUtils.join(longList, ',');
		req.getContext().put("_param", str);

	}

}
