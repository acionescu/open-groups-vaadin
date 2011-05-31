package ro.zg.open_groups.gui.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ro.zg.open_groups.gui.constants.OpenGroupsIconsSet;
import ro.zg.open_groups.gui.constants.OpenGroupsStyles;
import ro.zg.open_groups.managers.ApplicationConfigManager;
import ro.zg.open_groups.resources.OpenGroupsResources;
import ro.zg.opengroups.vo.Entity;
import ro.zg.opengroups.vo.EntityList;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Tree.CollapseEvent;
import com.vaadin.ui.Tree.CollapseListener;
import com.vaadin.ui.Tree.ExpandListener;

public class CausalHierarchyContainer extends VerticalLayout {

    /**
     * 
     */
    private static final long serialVersionUID = 988099253447545307L;
    /**
     * components
     */
    private ComboBox startDepthSelect;
    private Tree hierarchyTree;

    /**
     * values
     */
    private int startDepth = -1;
    private long maxDepth = 0;
    private int cacheDepth = 0;
    private Object selection;

    private void init() {
	/* start depth combo */
	CssLayout startDepthContainer = new CssLayout();
	startDepthContainer.setWidth("100%");
	startDepthContainer.addStyleName(OpenGroupsStyles.HIERARCHY_FILTERS_BAR);

	Label startDepthLabel = new Label(OpenGroupsResources.getMessage("hierarchy.start.depth"));
	startDepthLabel.setSizeUndefined();
	startDepthLabel.addStyleName(OpenGroupsStyles.HORIZONTAL);

	startDepthSelect = new ComboBox();
	startDepthSelect.setInvalidAllowed(false);
	startDepthSelect.setNullSelectionAllowed(false);
	startDepthSelect.setNewItemsAllowed(false);
	startDepthSelect.addStyleName(OpenGroupsStyles.HORIZONTAL);
	startDepthSelect.setWidth("55px");
	startDepthSelect.setImmediate(true);

	startDepthContainer.addComponent(startDepthLabel);
	startDepthContainer.addComponent(startDepthSelect);

//	CssLayout hierarchyTitleBar = new CssLayout();
//	hierarchyTitleBar.setWidth("100%");
//	hierarchyTitleBar.addStyleName(OpenGroupsStyles.HIERARCHY_TITLE_BAR);
//	Label title = new Label("Ierarhie cauzală");
//	title.addStyleName(OpenGroupsStyles.TITLE_LINK);
//	hierarchyTitleBar.addComponent(title);

//	addComponent(hierarchyTitleBar);
	addComponent(startDepthContainer);

	/* the tree */
	hierarchyTree = new Tree();
	hierarchyTree.setMultiSelect(false);
	hierarchyTree.setImmediate(true);
	hierarchyTree.setNullSelectionAllowed(true);
	hierarchyTree.addStyleName(OpenGroupsStyles.HIERARCHY_TREE);
	// hierarchyTree.setSizeFull();
	hierarchyTree.setWidth("100%");
	hierarchyTree.setHeight("99.6%");
	hierarchyTree.setContainerDataSource(new HierarchicalContainer());
	hierarchyTree.addContainerProperty("depth", Integer.class, null);
	hierarchyTree.addListener(new CollapseListener() {

	    @Override
	    public void nodeCollapse(CollapseEvent event) {
		Object itemId = event.getItemId();
		/* remove the subhierarchy */
		removeSubhierarchy(itemId);
	    }
	});

	// CssLayout treeContainer = new CssLayout();
	// treeContainer.setWidth("100%");
	// treeContainer.setHeight("99.6%");
	// treeContainer.setMargin(true);
	// treeContainer.addStyleName(OpenGroupsStyles.HIERARCHY_TREE);
	// treeContainer.addComponent(hierarchyTree);
	addComponent(hierarchyTree);
	setExpandRatio(hierarchyTree, 1);
    }

    private void removeSubhierarchy(Object parentId) {
	List<Object> subhierarchy = getSubhierarchy(parentId, new ArrayList<Object>());
	for (Object itemId : subhierarchy) {
	    hierarchyTree.removeItem(itemId);
	}
    }

    private List<Object> getSubhierarchy(Object parentId, List<Object> list) {
	if (!hierarchyTree.hasChildren(parentId)) {
	    return list;
	}
	for (Object childId : hierarchyTree.getChildren(parentId)) {
	    getSubhierarchy(childId, list);
	    list.add(childId);
	}
	return list;
    }

    public void construct() {
	removeAllComponents();
	init();
    }

    private void populateStartDepthSelect() {
	startDepthSelect.removeAllItems();
	for (int i = 0; i <= maxDepth; i++) {
	    startDepthSelect.addItem(i);
	}
	if (startDepth < 0 || startDepth > maxDepth) {
	    setStartDepth(0);
	} else {
	    startDepthSelect.select(startDepth);
	}
    }

    public void updateHierarchy(EntityList hierarchyList, boolean refresh) {
	if (refresh) {
	    hierarchyTree.removeAllItems();
	}
	for (Entity e : hierarchyList.getItemsList()) {
	    long parentId = e.getParentEntityId();
	    long currentId = e.getId();
	    /* add this node */
	    Item currentItem = hierarchyTree.addItem(currentId);
	    hierarchyTree.setItemCaption(currentId, e.getTitle());
	    hierarchyTree.setItemIcon(currentId, OpenGroupsResources.getIcon(e.getComplexType().toLowerCase(),
		    OpenGroupsIconsSet.SMALL));
	    boolean hasNonLeafChildren = hasNonLeafChildren(e);
	    hierarchyTree.setChildrenAllowed(currentId, hasNonLeafChildren);
	    if (hasNonLeafChildren) {

	    }
	    hierarchyTree.collapseItem(currentId);
	    /* see if the parent is present in the tree */
	    Item parentItem = hierarchyTree.getItem(parentId);
	    /* the parent is present in the tree */
	    if (parentItem != null) {
		hierarchyTree.setChildrenAllowed(parentId, true);
		hierarchyTree.setParent(currentId, parentId);
		/* use the depth of the parrent as a reference */
		currentItem.getItemProperty("depth").setValue(
			(Integer) parentItem.getItemProperty("depth").getValue() + 1);
	    } else {
		currentItem.getItemProperty("depth").setValue(e.getDepth());
	    }
	}
    }

    /**
     * Checks if the specified entity has children than have a non leaf type ( are part of a causal hierarchy)
     * 
     * @param e
     * @return
     */
    private boolean hasNonLeafChildren(Entity e) {
	Map<String, Long> subtypeEntitiesCount = e.getSubtypeEntitiesCount();
	List<String> nonLeafTypes = ApplicationConfigManager.getInstance().getNonLeafTypes();
	for (String nlt : nonLeafTypes) {
	    Long value = subtypeEntitiesCount.get(nlt.toLowerCase());
	    if (value != null && value > 0) {
		return true;
	    }
	}
	return false;
    }

    private void populateDummy() {
	final Object[][] planets = new Object[][] { new Object[] { "Mercury" }, new Object[] { "Venus" },
		new Object[] { "Earth", "The Moon" }, new Object[] { "Mars", "Phobos", "Deimos" },
		new Object[] { "Jupiter", "Io", "Europa", "Ganymedes", "Callisto" },
		new Object[] { "Saturn", "Titan", "Tethys", "Dione", "Rhea", "Iapetus" },
		new Object[] { "Uranus", "Miranda", "Ariel", "Umbriel", "Titania", "Oberon" },
		new Object[] { "Neptune", "Triton", "Proteus", "Nereid", "Larissa" } };

	/* Add planets as root items in the tree. */
	for (int i = 0; i < planets.length; i++) {
	    String planet = (String) (planets[i][0]);
	    hierarchyTree.addItem(planet);

	    if (planets[i].length == 1) {
		// The planet has no moons so make it a leaf.
		hierarchyTree.setChildrenAllowed(planet, false);
	    } else {
		// Add children (moons) under the planets.
		for (int j = 1; j < planets[i].length; j++) {
		    String moon = (String) planets[i][j];

		    // Add the item as a regular item.
		    hierarchyTree.addItem(moon);

		    // Set it to be a child.
		    hierarchyTree.setParent(moon, planet);

		    // Make the moons look like leaves.
		    hierarchyTree.setChildrenAllowed(moon, false);
		}

		// Expand the subtree.
		hierarchyTree.expandItemsRecursively(planet);
	    }
	}
    }

    public void setStartDepthChangedListener(ValueChangeListener listener) {
	startDepthSelect.addListener(listener);
    }

    public void setTreeExpandListener(ExpandListener listener) {
	hierarchyTree.addListener(listener);
    }

    public void setSelectionChangedListener(ValueChangeListener listener) {
	hierarchyTree.addListener(listener);
    }

    /**
     * @return the maxDepth
     */
    public long getMaxDepth() {
	return maxDepth;
    }

    /**
     * @param maxDepth
     *            the maxDepth to set
     */
    public void setMaxDepth(long maxDepth) {
	if (maxDepth >= 0 && maxDepth != this.maxDepth) {
	    this.maxDepth = maxDepth;
	    populateStartDepthSelect();
	}
    }

    /**
     * @return the startDepth
     */
    public int getStartDepth() {
	return startDepth;
    }

    /**
     * @param startDepth
     *            the startDepth to set
     */
    public void setStartDepth(int startDepth) {
	if (startDepth >= 0 && startDepth <= maxDepth && this.startDepth != startDepth) {
	    this.startDepth = startDepth;
	    Object startDepthValue = startDepthSelect.getValue();
	    if (startDepthValue == null || startDepth != (Integer) startDepthValue) {
		startDepthSelect.select(startDepth);
	    }
	}
    }

    /**
     * @return the cacheDepth
     */
    public int getCacheDepth() {
	return cacheDepth;
    }

    /**
     * @param cacheDepth
     *            the cacheDepth to set
     */
    public void setCacheDepth(int cacheDepth) {
	this.cacheDepth = cacheDepth;
    }

    public void setSelected(Object id) {
	Object selected = hierarchyTree.getValue();
	if(selected == null){
	    if(id==null) {
		return;
	    }
	}
	else if( selected.equals(id)) {
	    return;
	}
	
	if (hierarchyTree.containsId(id)) {
	    hierarchyTree.select(id);
	}
	else {
	    hierarchyTree.select(null);
	}
    }

}
