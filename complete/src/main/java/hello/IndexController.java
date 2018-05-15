package hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController
{
    @GetMapping({ "/", "/index" })
    public String index()
    {
        return 
            "<html><body>" +
                "<h1>Hello Spring WS!</h1>" +
            "</body></html>";
    }
}
