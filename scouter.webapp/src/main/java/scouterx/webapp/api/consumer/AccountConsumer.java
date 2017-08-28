/*
 *  Copyright 2015 the original author or authors.
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package scouterx.webapp.api.consumer;

import scouter.lang.pack.MapPack;
import scouter.lang.value.BooleanValue;
import scouter.lang.value.Value;
import scouter.net.RequestCmd;
import scouter.util.CipherUtil;
import scouterx.client.net.TcpProxy;
import scouterx.client.server.Server;
import scouterx.client.server.ServerManager;
import scouterx.webapp.api.model.User;

import javax.validation.ValidationException;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 8. 27.
 */
public class AccountConsumer {
    public boolean login(final Server server, final User user) {
        Server _server = server;

        if (_server == null) {
            if (ServerManager.getInstance().getServerCount() != 1) {
                throw new ValidationException("parameter:server should be not null!");
            }
            _server = ServerManager.getInstance().getDefaultServer();
        }

        MapPack param = new MapPack();
        param.put("id", user.getId());
        param.put("pass", CipherUtil.sha256(user.getPassword()));

        Value value = TcpProxy.getTcpProxy(_server.getId()).getSingleValue(RequestCmd.CHECK_LOGIN, param);
        return ((BooleanValue) value).value;
    }
}
