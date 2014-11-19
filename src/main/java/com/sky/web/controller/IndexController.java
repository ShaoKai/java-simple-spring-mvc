package com.sky.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	private String INDEX_PAGE = "index";

	/*
	 * ============== View =======================================
	 */
	@RequestMapping(value = { "/*" })
	public String index(HttpSession session, Model model) {

		String now = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());
		logger.info(now);
		return INDEX_PAGE;
	}
}
