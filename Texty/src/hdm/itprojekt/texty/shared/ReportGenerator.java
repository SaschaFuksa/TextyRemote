package hdm.itprojekt.texty.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("report")
public interface ReportGenerator extends RemoteService{
	
	public void init() throws IllegalArgumentException;
	
}
