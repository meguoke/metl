package org.jumpmind.symmetric.is.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Component extends AbstractObjectWithSettings {

    private static final long serialVersionUID = 1L;

    String type;

    boolean shared;

    String name;

    Resource resource;

    Model inputModel;

    Model outputModel;

    String projectVersionId;

    boolean deleted = false;

    String rowId = UUID.randomUUID().toString();

    List<ComponentEntitySetting> entitySettings;

    List<ComponentAttributeSetting> attributeSettings;

    public Component() {
    }

    public Component(String id) {
        this();
        this.id = id;
    }

    public Component(Resource resource, Model inputModel, Model outputModel,
            List<ComponentEntitySetting> entitySettings,
            List<ComponentAttributeSetting> attributeSettings, Setting... settings) {
        super(settings);
        this.resource = resource;
        this.inputModel = inputModel;
        this.outputModel = outputModel;
        this.entitySettings = entitySettings;
        this.attributeSettings = attributeSettings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setInputModel(Model inputModel) {
        this.inputModel = inputModel;
    }

    public void setOutputModel(Model outputModel) {
        this.outputModel = outputModel;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public String getInputModelId() {
        return inputModel != null ? inputModel.getId() : null;
    }

    public void setInputModelId(String inputModeldId) {
        if (inputModeldId != null) {
            this.inputModel = new Model(inputModeldId);
        } else {
            this.inputModel = null;
        }
    }

    public String getOutputModelId() {
        return outputModel != null ? outputModel.getId() : null;
    }

    public void setOutputModelId(String outputModelId) {
        if (outputModelId != null) {
            this.outputModel = new Model(outputModelId);
        } else {
            this.outputModel = null;
        }
    }

    public String getResourceId() {
        return resource != null ? resource.getId() : null;
    }

    public void setResourceId(String resourceId) {
        if (resourceId != null) {
            this.resource = new Resource(resourceId);
        } else {
            this.resource = null;
        }
    }

    public Model getInputModel() {
        return inputModel;
    }

    public Model getOutputModel() {
        return outputModel;
    }

    public List<ComponentEntitySetting> getEntitySettings() {
        return entitySettings;
    }

    public void setEntitySettings(List<ComponentEntitySetting> entitySettings) {
        this.entitySettings = entitySettings;
    }

    public List<ComponentAttributeSetting> getAttributeSettings() {
        return attributeSettings;
    }

    public void setAttributeSettings(List<ComponentAttributeSetting> attributeSettings) {
        this.attributeSettings = attributeSettings;
    }

    public void addAttributeSetting(ComponentAttributeSetting attributeSetting) {
        if (attributeSettings == null) {
            attributeSettings = new ArrayList<ComponentAttributeSetting>();
        }
        attributeSettings.add(attributeSetting);
    }

    public ComponentAttributeSetting getSingleAttributeSetting(String attributeId, String name) {
        List<ComponentAttributeSetting> list = getAttributeSetting(attributeId, name);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<ComponentAttributeSetting> getAttributeSetting(String attributeId, String name) {
        List<ComponentAttributeSetting> list = new ArrayList<ComponentAttributeSetting>();
        for (ComponentAttributeSetting setting : attributeSettings) {
            if (setting.getAttributeId().equals(attributeId)
                    && setting.getName().equalsIgnoreCase(name)) {
                list.add(setting);
            }
        }
        return list;
    }

    @Override
    protected Setting createSettingData() {
        return new ComponentSetting(id);
    }

    public void setProjectVersionId(String projectVersionId) {
        this.projectVersionId = projectVersionId;
    }

    public String getProjectVersionId() {
        return projectVersionId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getRowId() {
        return rowId;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }
}