package dakakeen.dakakeen;

/**
 * Created by moath on 4/16/2017.
 */

public interface ResponseHandler {

    void onSuccess(byte[] responseBody);

    void onFailure(byte[] responseBody);
}
