/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.executor.transport.executionphases;

import io.crate.Streamer;
import io.crate.action.job.JobResponse;
import io.crate.jobs.PageBucketReceiver;
import org.elasticsearch.action.ActionListener;

import javax.annotation.Nonnull;
import java.util.List;

class SetBucketActionListener extends SetBucketAction implements ActionListener<JobResponse> {

    private final Streamer<?>[] streamers;

    SetBucketActionListener(List<PageBucketReceiver> pageBucketReceivers, int bucketIdx, InitializationTracker initializationTracker) {
        super(pageBucketReceivers, bucketIdx, initializationTracker);
        streamers = pageBucketReceivers.get(0).streamers();
    }

    @Override
    public void onResponse(JobResponse jobResponse) {
        jobResponse.streamers(streamers);
        setBuckets(jobResponse.directResponse());
    }

    @Override
    public void onFailure(@Nonnull Throwable t) {
        failed(t);
    }
}
