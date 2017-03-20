package haibi.com.creeper.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import haibi.com.creeper.entity.Schedule;

@RestController
public class TvsouController {

	Logger logger = LoggerFactory.getLogger(TvsouController.class);
	@RequestMapping(value = "/{clazz}/{channel}", method = RequestMethod.GET)
    public Schedule say(@PathVariable String clazz,@PathVariable String channel) {

		Document doc = null;
		Schedule schedule = new Schedule();
		List<Schedule.Item> items = new ArrayList<>();
		try {
			doc = Jsoup.connect("http://www.tvsou.com/epg/"+channel+"?class="+clazz).get();
			ArrayList<Element> lis = doc.select(".play-time-more .relative");
			schedule.setName(doc.select(".channel-top .title").text());
			lis.forEach((li)->{
				if(!li.hasClass("absolute")){
					Schedule.Item item = new Schedule.Item();
					item.setName(li.select("a").text());
					item.setTime(li.select("span").text());
					items.add(item);
				}
				
			});
			schedule.setItems(items);
		} catch (IOException e) {
			logger.error("获取网页失败",e);
		}
        return schedule;
    }
}
