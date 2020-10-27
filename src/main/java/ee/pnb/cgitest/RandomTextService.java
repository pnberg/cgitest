package ee.pnb.cgitest;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomTextService {

  public String generateRandomText() {
    Lorem lorem = LoremIpsum.getInstance();
    return lorem.getWords(1, 10);
  }

}
