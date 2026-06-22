# HTTP API

This document describes the local HTTP endpoint exposed by **nw-concurrency**.

## Server

The server is started by [`NeedlemanOrchestrator.Main`](../concurrent-needle-wunsch/NeedlemanOrchestrator/src/com/codigofacilito/needleman/orchestrator/Main.java) via [`LocalHttpServer`](../concurrent-needle-wunsch/WebRequest/src/com/codigofacilito/sequence/api/server/LocalHttpServer.java).

| Setting | Value |
|---------|-------|
| Host | `localhost` |
| Port | `8080` |
| Base URL | `http://localhost:8080/api/sequence` |

On startup, the server prints:

```
Server started http://localhost:8080/api/sequence
```

## Endpoint

### `POST /api/sequence`

Triggers sequence fetch, alignment, and output.

#### Request

| Field | Value |
|-------|-------|
| Method | `POST` |
| Body | Empty (no request body is read) |
| Headers | None required |

Sequence IDs are **not** passed in the request. They are read from `request.properties` at runtime:

```properties
req.seq-a-id=ENSG00000239615
req.seq-b-id=ENSG00000239617
```

#### Processing steps

1. Validate that the request method is `POST`
2. Load sequence IDs and Ensembl URL from `request.properties`
3. Fetch both sequences from Ensembl in parallel
4. Run Needleman-Wunsch alignment (see [Algorithm](algorithm.md))
5. Write aligned sequences to file or console (see [Configuration](configuration.md))

#### Responses

| Status | Condition | Body |
|--------|-----------|------|
| `201 Created` | Alignment completed successfully | `File created successfully` |
| `405 Method Not Allowed` | Any method other than `POST` | Empty |

#### Example

```bash
curl -X POST http://localhost:8080/api/sequence
```

Expected response:

```
HTTP/1.1 201 Created
File created successfully
```

Alignment results are **not** returned in the HTTP response. They are written to the configured output destination (default: `result.txt` in the JAR directory).

## Ensembl integration

Sequences are fetched from the [Ensembl REST API](https://rest.ensembl.org/) using the URL template in `request.properties`:

```properties
req.url=https://rest.ensembl.org/sequence/id/%s?type=cdna;content-type=application/json
```

The `%s` placeholder is replaced with each Ensembl gene ID. [`SequenceWebClientImpl`](../concurrent-needle-wunsch/WebRequest/src/com/codigofacilito/sequence/api/client/impl/SequenceWebClientImpl.java) uses `java.net.http.HttpClient` and extracts the sequence string from the JSON response via regex (`"seq":"..."`).

### Default gene IDs

| Property | Default | Description |
|----------|---------|-------------|
| `req.seq-a-id` | `ENSG00000239615` | First sequence Ensembl gene ID |
| `req.seq-b-id` | `ENSG00000239617` | Second sequence Ensembl gene ID |

### Finding other gene IDs

Sample workflow for human VEGFA:

1. Look up gene ID:
   ```
   https://rest.ensembl.org/xrefs/symbol/homo_sapiens/VEGFA?content-type=application/json
   ```
   Response includes `"id": "ENSG00000112715"`.

2. Expand gene details:
   ```
   https://rest.ensembl.org/lookup/id/ENSG00000112715?expand=1;content-type=application/json
   ```
   Look for `protein_coding` biotype transcripts.

3. Set the chosen IDs in `request.properties` and restart the server (or ensure properties are reloaded — currently loaded once per request via `RequestProperties.getInstance()`).

## Limitations

- **No JSON API** — the endpoint does not accept or return structured alignment data
- **No input validation** — sequences from Ensembl are used as-is; no length limits or character checks
- **Configuration-driven IDs** — changing sequences requires editing `request.properties`, not the HTTP request
- **No authentication** — the local server has no auth layer
- **Single endpoint** — only `/api/sequence` is exposed

## Related documentation

- [Configuration](configuration.md) — `request.properties` reference
- [Architecture](architecture.md) — end-to-end request flow diagram
- [Concurrency](concurrency.md) — parallel Ensembl fetch details
