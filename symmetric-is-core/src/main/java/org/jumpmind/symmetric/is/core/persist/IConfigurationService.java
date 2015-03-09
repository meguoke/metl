package org.jumpmind.symmetric.is.core.persist;

import java.util.List;

import org.jumpmind.symmetric.is.core.model.AbstractObject;
import org.jumpmind.symmetric.is.core.model.Agent;
import org.jumpmind.symmetric.is.core.model.AgentDeployment;
import org.jumpmind.symmetric.is.core.model.AgentSummary;
import org.jumpmind.symmetric.is.core.model.Flow;
import org.jumpmind.symmetric.is.core.model.FlowStep;
import org.jumpmind.symmetric.is.core.model.FlowStepLink;
import org.jumpmind.symmetric.is.core.model.FlowVersion;
import org.jumpmind.symmetric.is.core.model.FlowVersionSummary;
import org.jumpmind.symmetric.is.core.model.Resource;
import org.jumpmind.symmetric.is.core.model.Folder;
import org.jumpmind.symmetric.is.core.model.FolderType;
import org.jumpmind.symmetric.is.core.model.Model;
import org.jumpmind.symmetric.is.core.model.ModelAttribute;
import org.jumpmind.symmetric.is.core.model.ModelAttributeRelationship;
import org.jumpmind.symmetric.is.core.model.ModelEntity;
import org.jumpmind.symmetric.is.core.model.ModelEntityRelationship;
import org.jumpmind.symmetric.is.core.model.ModelVersion;

public interface IConfigurationService {

    public List<Folder> findFolders(FolderType type);
    
    public FlowVersion findFlowVersion(String id);

    public void deleteFolder(String folderId);

    public void delete(Agent agent);
    
    public void delete(AgentDeployment agentDeployment);
    
    public void delete(Flow flow);
    
    public void delete(FlowVersion flow, FlowStep flowStep);
    
    public void delete(FlowStepLink link);
    
    public void delete(Resource resource);
    
    public boolean isDeployed(FlowVersion flowVersion);
    
    public boolean isDeployed(Flow flow);
    
    public List<Flow> findFlowsInFolder(Folder folder);
    
    public List<Model> findModelsInFolder(Folder folder);
    
    public Resource findResource(String id);
    
    public List<Resource> findResourcesInFolder(Folder folder);
    
    public List<Resource> findResourcesByTypes(String ... types);
    
    public List<Agent> findAgentsInFolder(Folder folder);
    
    public List<Agent> findAgentsForHost(String hostName);
    
    public List<AgentDeployment> findAgentDeploymentsFor(FlowVersion flowVersion);
    
    public void deleteFlowVersion(FlowVersion flowVersion);

    public void refresh(FlowVersion flowVersion);
    
    public void refresh(Agent agent);
    
    public void refresh(Resource resource);
    
    public void save(Resource resource);
    
    public void save(FlowStep flowStep);

    public void save(AbstractObject obj);

    public void save(FlowVersion flowVersion);
    
    public List<FlowVersionSummary> findUndeployedFlowVersionSummary(String agentId);
    
    public List<AgentSummary> findUndeployedAgentsFor(String flowVersionId);
    
    public void delete(Model model);
    
    public void delete(ModelVersion modelVersion);
    
    public void delete(ModelEntity modelEntity);
    
    public void delete(ModelAttribute modelAttribute);
    
    public void delete(ModelEntityRelationship modelEntityRelationship);
    
    public void delete(ModelAttributeRelationship modelAttributeRelationship);
    
    public void refresh(Model model);
    
    public void refresh(ModelVersion modelVersion);
    
    public void refresh(ModelEntity modelEntity);
    
    public void refresh(ModelAttribute modelAttribute);
    
    public void refresh(ModelEntityRelationship modelEntityRelationship);
    
    public void refresh(ModelAttributeRelationship modelAttributeRelationship);
    
    public void save(ModelVersion modelVersion);
    
    public void save(ModelEntity modelEntity);
    
    public void save(ModelEntityRelationship modelEntityRelationship);

}
