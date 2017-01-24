/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.main.tool.project;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.tool.map.catalog.LayerLoader;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;

/**
 * Layer info dialog. Responsible to show layer info and features list.
 * 
 * @author geowe.org
 * @since 15-09-2016
 * @author rafa@geoew.org changed design
 *
 */
@ApplicationScoped
public class OpenProjectDialog extends Dialog {
	public static final int FEATURES_PER_PAGE = 50;
	private FormPanel uploadPanel;
	private FileUploadField file;
	

	public OpenProjectDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UIMessages.INSTANCE.openProject());
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setPixelSize(350, 300);
		this.setModal(true);
		this.setResizable(false);
		//this.setHideOnButtonClick(true);
	}
	
	public void clear() {		
		this.file.clear();		
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
		
	}

	private Widget createPanel() {

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setPixelSize(490, 400);
		vPanel.setSpacing(5);
		vPanel.add(getFilePanel());

		return vPanel;
	}

	private FormPanel getFilePanel() {
		VerticalLayoutContainer layoutContainer = new VerticalLayoutContainer();

		file = new FileUploadField();
		file.setName(UIMessages.INSTANCE.gdidFileUploadFieldText());
		file.setAllowBlank(false);

		layoutContainer.add(new FieldLabel(file, UIMessages.INSTANCE.file()),
				new VerticalLayoutData(-18, -1));
		layoutContainer.add(new Label(UIMessages.INSTANCE.maxFileSizeText()),
				new VerticalLayoutData(-18, -1));

		uploadPanel = new FormPanel();
		uploadPanel.setMethod(Method.POST);
		uploadPanel.setEncoding(Encoding.MULTIPART);
		uploadPanel.setAction("fileupload.do");
		uploadPanel.setWidget(layoutContainer);

		return uploadPanel;
	}

	public FormPanel getUploadPanel() {
		return uploadPanel;
	}		
}
