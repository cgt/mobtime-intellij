package name.cgt.mobtimeintellij;

import com.intellij.openapi.components.Service;

@Service
public final class MyService {
    private int count = 0;

    public void increaseCount() {
        count += 1;
    }

    public int getCount() {
        return count;
    }
}
