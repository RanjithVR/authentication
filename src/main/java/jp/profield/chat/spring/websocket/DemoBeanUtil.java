package jp.profield.chat.spring.websocket;



import jp.profield.chat.service.ChatService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DemoBeanUtil implements ApplicationContextAware {

	private static ApplicationContext appCxt;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCxt = applicationContext;
    }
    
    
    public static ChatService getChatService() throws BeansException {
    	
        return (ChatService) appCxt.getAutowireCapableBeanFactory().getBean("chatService");
    }

}
