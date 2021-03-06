/*
 * This file is derived from the file WireConverterFactoryTest.java, in the
 * Retrofit project.  The original license header is reproduced below:
 */

/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.infinum.thrifty;

import com.infinum.thrifty.kotlin.BrokenPhone;
import com.infinum.thrifty.java.Phone;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import okio.ByteString;
import org.junit.Rule;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public final class ThriftyConverterFactoryTest {
    interface Service {
        @GET("/")
        Call<Phone> get();

        @POST("/")
        Call<Phone> post(@Body Phone impl);

        @GET("/")
        Call<com.infinum.thrifty.kotlin.Phone> getKt();

        @POST("/")
        Call<com.infinum.thrifty.kotlin.Phone> postKt(@Body com.infinum.thrifty.kotlin.Phone impl);

        @GET("/")
        Call<String> getWrongClass();

        @GET("/")
        Call<List<String>> getWrongType();

        @POST("/")
        Call<String> postWrongClass(@Body String body);

        @POST("/")
        Call<List<String>> postWrongType(@Body List<String> body);
    }

    interface BrokenService {
        @GET("/")
        Call<BrokenPhone> get();
    }

    @Rule
    public final MockWebServer server = new MockWebServer();

    private Service createService(ProtocolType protocolType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(ThriftyConverterFactory.create(protocolType))
                .build();
        return retrofit.create(Service.class);
    }

    @Test
    public void serializeAndDeserializeBinary() throws IOException, InterruptedException {
        serializeAndDeserialize(ProtocolType.BINARY, "(519) 867-5309", "CwABAAAADig1MTkpIDg2Ny01MzA5AA==", true);
        serializeAndDeserializeKt(ProtocolType.BINARY, "(519) 867-5309", "CwABAAAADig1MTkpIDg2Ny01MzA5AA==", true);
    }

    @Test
    public void serializeAndDeserializeCompact() throws IOException, InterruptedException {
        serializeAndDeserialize(ProtocolType.COMPACT, "(519) 867-5309", "GA4oNTE5KSA4NjctNTMwOQA=", true);
        serializeAndDeserializeKt(ProtocolType.COMPACT, "(519) 867-5309", "GA4oNTE5KSA4NjctNTMwOQA=", true);
    }

    @Test
    public void serializeAndDeserializeJson() throws IOException, InterruptedException {
        serializeAndDeserialize(ProtocolType.JSON, "(519) 867-5309", "{\"1\":{\"str\":\"(519) 867-5309\"}}", false);
        serializeAndDeserializeKt(ProtocolType.JSON, "(519) 867-5309", "{\"1\":{\"str\":\"(519) 867-5309\"}}", false);
    }

    private void serializeAndDeserialize(ProtocolType type, String phoneNumber, String body, boolean isBodyBase64) throws IOException, InterruptedException {
        Service service = createService(type);

        ByteString bodyByteString = isBodyBase64 ? ByteString.decodeBase64(body) : ByteString.encodeUtf8(body);
        server.enqueue(new MockResponse().setBody(new Buffer().write(bodyByteString)));

        Phone phone = new Phone.Builder().number(phoneNumber).build();
        Call<Phone> call = service.post(phone);
        Response<Phone> response = call.execute();
        Phone bodyPhone = response.body();
        assertThat(bodyPhone.number).isEqualTo(phoneNumber);

        RecordedRequest request = server.takeRequest();

        assertThat(request.getBody().readByteString()).isEqualTo(bodyByteString);
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/x-thrift");
    }

    private void serializeAndDeserializeKt(ProtocolType type, String phoneNumber, String body, boolean isBodyBase64) throws IOException, InterruptedException {
        Service service = createService(type);

        ByteString bodyByteString = isBodyBase64 ? ByteString.decodeBase64(body) : ByteString.encodeUtf8(body);
        server.enqueue(new MockResponse().setBody(new Buffer().write(bodyByteString)));

        com.infinum.thrifty.kotlin.Phone phone = new com.infinum.thrifty.kotlin.Phone.Builder().number(phoneNumber).build();
        Call<com.infinum.thrifty.kotlin.Phone> call = service.postKt(phone);
        Response<com.infinum.thrifty.kotlin.Phone> response = call.execute();
        com.infinum.thrifty.kotlin.Phone bodyPhone = response.body();
        assertThat(bodyPhone.number).isEqualTo(phoneNumber);

        RecordedRequest request = server.takeRequest();

        assertThat(request.getBody().readByteString()).isEqualTo(bodyByteString);
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/x-thrift");
    }

    @Test
    public void serializeWrongClassBinary() throws IOException {
        serializeWrongClass(ProtocolType.BINARY);
    }

    @Test
    public void serializeWrongClassCompact() throws IOException {
        serializeWrongClass(ProtocolType.COMPACT);
    }

    @Test
    public void serializeWrongClassJson() throws IOException {
        serializeWrongClass(ProtocolType.JSON);
    }

    private void serializeWrongClass(ProtocolType type) throws IOException {
        Service service = createService(type);

        try {
            service.postWrongClass("string");
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(""
                    + "Unable to create @Body converter for class java.lang.String (parameter #1)\n"
                    + "    for method Service.postWrongClass");
            assertThat(e.getCause()).hasMessageMatching("(?s).*Tried:.*ThriftyConverterFactory.*");
        }
    }

    @Test
    public void deserializeWrongClassBinary() throws IOException {
        deserializeWrongClass(ProtocolType.BINARY, "CwABAAAADig1MTkpIDg2Ny01MzA5AA==", true);
    }

    @Test
    public void deserializeWrongClassCompact() throws IOException {
        deserializeWrongClass(ProtocolType.COMPACT, "GA4oNTE5KSA4NjctNTMwOQA=", true);
    }

    @Test
    public void deserializeWrongClassJson() throws IOException {
        deserializeWrongClass(ProtocolType.JSON, "{\"1\":{\"str\":\"(519) 867-5309\"}}", false);
    }

    private void deserializeWrongClass(ProtocolType type, String body, boolean isBodyBase64) throws IOException {
        Service service = createService(type);
        ByteString bodyByteString = isBodyBase64 ? ByteString.decodeBase64(body) : ByteString.encodeUtf8(body);
        server.enqueue(new MockResponse().setBody(new Buffer().write(bodyByteString)));

        try {
            service.getWrongClass();
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(""
                    + "Unable to create converter for class java.lang.String\n"
                    + "    for method Service.getWrongClass");
            assertThat(e.getCause()).hasMessageMatching("(?s).*Tried:.*ThriftyConverterFactory.*");
        }
    }

    @Test
    public void serializeWrongTypeBinary() throws IOException {
        serializeWrongType(ProtocolType.BINARY);
    }

    @Test
    public void serializeWrongTypeCompact() throws IOException {
        serializeWrongType(ProtocolType.COMPACT);
    }

    @Test
    public void serializeWrongTypeJson() throws IOException {
        serializeWrongType(ProtocolType.JSON);
    }

    private void serializeWrongType(ProtocolType type) throws IOException {
        Service service = createService(type);

        try {
            List<String> body = new ArrayList<>();
            service.postWrongType(body);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(""
                    + "Unable to create @Body converter for java.util.List<java.lang.String> (parameter #1)\n"
                    + "    for method Service.postWrongType");
            assertThat(e.getCause()).hasMessageMatching("(?s).*Tried:.*ThriftyConverterFactory.*");
        }
    }

    @Test
    public void deserializeWrongTypeBinary() throws IOException {
        deserializeWrongType(ProtocolType.BINARY, "CwABAAAADig1MTkpIDg2Ny01MzA5AA==", true);
    }

    @Test
    public void deserializeWrongTypeCompact() throws IOException {
        deserializeWrongType(ProtocolType.COMPACT, "GA4oNTE5KSA4NjctNTMwOQA=", true);
    }

    @Test
    public void deserializeWrongTypeJson() throws IOException {
        deserializeWrongType(ProtocolType.JSON, "{\"1\":{\"str\":\"(519) 867-5309\"}}", false);
    }

    private void deserializeWrongType(ProtocolType type, String body, boolean isBodyBase64) throws IOException {
        Service service = createService(type);
        ByteString bodyByteString = isBodyBase64 ? ByteString.decodeBase64(body) : ByteString.encodeUtf8(body);
        server.enqueue(new MockResponse().setBody(new Buffer().write(bodyByteString)));

        try {
            service.getWrongType();
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage(""
                    + "Unable to create converter for java.util.List<java.lang.String>\n"
                    + "    for method Service.getWrongType");
            assertThat(e.getCause()).hasMessageMatching("(?s).*Tried:.*ThriftyConverterFactory.*");
        }
    }

    @Test
    public void deserializeWrongValueBinary() throws IOException {
        Service service = createService(ProtocolType.BINARY);
        ByteString encoded = ByteString.decodeBase64("////");
        server.enqueue(new MockResponse().setBody(new Buffer().write(encoded)));

        Call<?> call = service.get();
        try {
            call.execute();
            fail();
        } catch (ProtocolException ignored) {
        }
    }

    @Test
    public void deserializeWrongValueCompact() throws IOException {
        Service service = createService(ProtocolType.COMPACT);
        ByteString encoded = ByteString.decodeBase64("////");
        server.enqueue(new MockResponse().setBody(new Buffer().write(encoded)));

        Call<?> call = service.get();
        try {
            call.execute();
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void deserializeWrongValueJson() throws IOException {
        Service service = createService(ProtocolType.JSON);
        ByteString encoded = ByteString.encodeUtf8("{x}");
        server.enqueue(new MockResponse().setBody(new Buffer().write(encoded)));

        Call<?> call = service.get();
        try {
            call.execute();
            fail();
        } catch (ProtocolException ignored) {
        }
    }

    @Test
    public void generatedClassWithoutAdapter() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(ThriftyConverterFactory.create(ProtocolType.BINARY))
                .build();
        BrokenService brokenService = retrofit.create(BrokenService.class);

        try {
            brokenService.get();
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void protocolType() throws IOException {
        assertThat(ProtocolType.BINARY.type()).isEqualTo("binary");
    }

}
