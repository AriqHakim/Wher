package id.emergence.wher.data.remote.exceptions

import java.io.IOException

class ApiException(
    message: String,
) : IOException(message)
