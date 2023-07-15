package commands

import IEclairClient
import IResultWriter
import kotlinx.cli.*
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalCli::class)
class ConnectCommand(
    private val resultWriter: IResultWriter, private val eclairClient: IEclairClient
) : Subcommand("connect", "Connect to another lightning node") {
    private var password by option(
        ArgType.String, shortName = "p", description = "Password for the Eclair API"
    ).required()
    private var host by option(
        ArgType.String, shortName = "l", description = "Host URL for the Eclair API"
    ).default("http://localhost:8080")
    private var uri by argument(ArgType.String, description = "The URI in format 'nodeId@host:port'")

    override fun execute() = runBlocking {
        val connectionResult = eclairClient.connect(password, host, uri)
        if (connectionResult.isSuccess) {
            resultWriter.writeSuccess(connectionResult.getOrThrow())
        } else {
            resultWriter.writeError("Error connecting to $uri: ${connectionResult.exceptionOrNull()?.message}")
        }
    }
}
