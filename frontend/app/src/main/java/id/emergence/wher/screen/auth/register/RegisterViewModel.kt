package id.emergence.wher.screen.auth.register

import id.emergence.wher.domain.repository.AuthRepository
import id.emergence.wher.utils.base.BaseViewModel

class RegisterViewModel(
    private val auth: AuthRepository,
) : BaseViewModel()
