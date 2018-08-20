/*
 * DISCLAIMER
 *
 * Copyright 2017 ArangoDB GmbH, Cologne, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright holder is ArangoDB GmbH, Cologne, Germany
 */

package com.arangodb.loadtest.testcase;

import java.util.Collection;
import java.util.List;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoVertexCollection;
import com.arangodb.entity.BaseDocument;
import com.arangodb.loadtest.cli.CliOptions;
import com.arangodb.loadtest.util.KeyGen;

/**
 * 
 * @author Mark Vollmary
 *
 */
public class VertexReadTestCase extends ArangoTestCase {

	private final ArangoVertexCollection collection;
	private final KeyGen keyGen;
	private List<String> keys;

	public VertexReadTestCase(final ArangoDB.Builder builder, final CliOptions options, final int num,
		final Collection<Long> times, final KeyGen keyGen) {
		super(builder, options, num, times);
		this.keyGen = keyGen;
		collection = arango.db(options.getDatabase()).graph(options.getGraph())
				.vertexCollection(options.getVertexCollection());
	}

	@Override
	protected void _prepare() {
		final Integer batchSize = options.getBatchSize();
		keys = keyGen.generateKeys(batchSize);
	}

	@Override
	protected void _run() throws ArangoDBException {
		final Integer batchSize = options.getBatchSize();
		if (batchSize == 1) {
			final BaseDocument doc = collection.getVertex(keys.get(0), BaseDocument.class);
			if (doc == null) {
				throw new ArangoDBException(String.format("Failed to read vertex with key: %s", keys.get(0)));
			}
		} else {
			throw new IllegalArgumentException("'batchSize' not supported with testcase '" + TestCase.VERTEX_GET + "'");
		}
	}

}
