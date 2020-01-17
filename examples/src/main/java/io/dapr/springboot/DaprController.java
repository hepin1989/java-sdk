/*
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */

package io.dapr.springboot;

import io.dapr.actors.runtime.ActorRuntime;
import io.dapr.runtime.Dapr;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * SpringBoot Controller to handle callback APIs for Dapr.
 */
@RestController
public class DaprController {

  @GetMapping("/")
  public String index() {
    return "Greetings from Dapr!";
  }

  @GetMapping("/dapr/config")
  public String daprConfig() throws Exception {
    return ActorRuntime.getInstance().serializeConfig();
  }

  @GetMapping("/dapr/subscribe")
  public String daprSubscribe() throws Exception {
    return Dapr.getInstance().serializeSubscribedTopicList();
  }

  @PostMapping(path = "/{name}")
  @PutMapping(path = "/{name}")
  @DeleteMapping(path = "/{name}")
  @GetMapping(path = "/{name}")
  public Mono<byte[]> invokeMethodOrTopic(@PathVariable("name") String name,
                                          @RequestBody(required = false) byte[] body,
                                          @RequestHeader Map<String, String> header) {
    return Dapr.getInstance().handleInvocation(name, body, header);
  }

  @PostMapping(path = "/actors/{type}/{id}")
  public Mono<Void> activateActor(@PathVariable("type") String type,
                                  @PathVariable("id") String id) throws Exception {
    return ActorRuntime.getInstance().activate(type, id);
  }

  @DeleteMapping(path = "/actors/{type}/{id}")
  public Mono<Void> deactivateActor(@PathVariable("type") String type,
                                    @PathVariable("id") String id) throws Exception {
    return ActorRuntime.getInstance().deactivate(type, id);
  }

  @PutMapping(path = "/actors/{type}/{id}/method/{method}")
  public Mono<String> invokeActorMethod(@PathVariable("type") String type,
                                        @PathVariable("id") String id,
                                        @PathVariable("method") String method,
                                        @RequestBody(required = false) String body) {
    return ActorRuntime.getInstance().invoke(type, id, method, body);
  }

  @PutMapping(path = "/actors/{type}/{id}/method/timer/{timer}")
  public Mono<Void> invokeActorTimer(@PathVariable("type") String type,
                                     @PathVariable("id") String id,
                                     @PathVariable("timer") String timer) {
    return ActorRuntime.getInstance().invokeTimer(type, id, timer);
  }

  @PutMapping(path = "/actors/{type}/{id}/method/remind/{reminder}")
  public Mono<Void> invokeActorReminder(@PathVariable("type") String type,
                                        @PathVariable("id") String id,
                                        @PathVariable("reminder") String reminder,
                                        @RequestBody(required = false) String body) {
    return ActorRuntime.getInstance().invokeReminder(type, id, reminder, body);
  }

}