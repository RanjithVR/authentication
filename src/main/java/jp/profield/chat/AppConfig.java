package jp.profield.chat;


import jp.profield.chat.spring.websocket.DemoBeanUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	

    @Bean
    public DemoBeanUtil randomNameBeanUtil(){
        return new DemoBeanUtil();
    }
    
    
}
	