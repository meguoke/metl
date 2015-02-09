package org.jumpmind.symmetric.is.ui.views;

import javax.annotation.PostConstruct;

import org.jumpmind.symmetric.is.core.config.FolderType;
import org.jumpmind.symmetric.is.core.persist.IConfigurationService;
import org.jumpmind.symmetric.is.ui.support.Category;
import org.jumpmind.symmetric.is.ui.support.TopBarLink;
import org.jumpmind.symmetric.ui.common.UiComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalSplitPanel;

@UiComponent
@Scope(value = "ui")
@TopBarLink(category = Category.DESIGN, name = "Design", id = "design", icon = FontAwesome.SHARE_ALT, menuOrder = 1, useAsDefault = true)
public class DesignView extends HorizontalLayout implements View {

    private static final long serialVersionUID = 1L;
    
    @Autowired
    IConfigurationService configurationService;
    
    DesignNavigator designNavigator;
    
    @PostConstruct
    protected void init() {
        setSizeFull();
        setMargin(new MarginInfo(true, false, false, false));

        HorizontalSplitPanel rightSplit = new HorizontalSplitPanel();
        rightSplit.setSizeFull();
        rightSplit.setSplitPosition(250, Unit.PIXELS, true);
        

        HorizontalSplitPanel leftSplit = new HorizontalSplitPanel();
        leftSplit.setSizeFull();
        leftSplit.setSplitPosition(300, Unit.PIXELS);


        VerticalSplitPanel leftTopBottomSplit = new VerticalSplitPanel();
        leftTopBottomSplit.setSizeFull();
        leftTopBottomSplit.setSplitPosition(60, Unit.PERCENTAGE);
        designNavigator = new DesignNavigator(FolderType.DESIGN, configurationService);
        leftTopBottomSplit.setFirstComponent(designNavigator);
        leftTopBottomSplit.setSecondComponent(new DesignComponentPalette());

        
        leftSplit.setFirstComponent(leftTopBottomSplit);
        leftSplit.setSecondComponent(new TabSheet());
        rightSplit.setFirstComponent(leftSplit);
        rightSplit.setSecondComponent(new DesignPropertySheet());
        
        addComponent(rightSplit);
    }
    
    @Override
    public void enter(ViewChangeEvent event) {
        designNavigator.refresh();
    }

}
