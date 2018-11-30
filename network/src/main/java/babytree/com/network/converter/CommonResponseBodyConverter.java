package babytree.com.network.converter;

/**
 * Created by Administrator on 2017/1/19.
 */

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import babytree.com.network.exception.ApiException;
import babytree.com.network.model.MyResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class CommonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CommonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        MyResponse baseModel = null;
        try {
            baseModel = gson.fromJson(response, MyResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (baseModel != null) {
            if (baseModel.isCodeInvalid()) {
                if ("101021".equals(baseModel.status)) {
                    value.close();
                    throw new IOException("");
                } else {
                    value.close();
                    throw new ApiException(baseModel.status,"数据错误...");
                }
            }
        }

        try {
            JSONObject json = new JSONObject(response);
            String data = json.getString("data");
            if(TextUtils.equals("null",data)||TextUtils.isEmpty(data)){
                return (T) new String();
            }
            JsonReader jsonReader = gson.newJsonReader(new StringReader(data));
            return adapter.read(jsonReader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        } finally {
            value.close();
        }
    }
}
