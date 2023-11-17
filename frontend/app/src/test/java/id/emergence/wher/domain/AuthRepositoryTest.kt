package id.emergence.wher.domain

import app.cash.turbine.test
import id.emergence.wher.TestDispatcherRule
import id.emergence.wher.data.remote.exceptions.ApiException
import id.emergence.wher.domain.model.LoginData
import id.emergence.wher.domain.model.RegisterData
import id.emergence.wher.domain.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthRepositoryTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @MockK
    lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login success`() =
        runTest {
            val expected = "dummyToken"
            coEvery { repository.login(any()) } returns
                flow {
                    emit(Result.success(expected))
                }

            val actual = repository.login(LoginData("", ""))
            actual.test {
                val data = awaitItem()
                assertEquals(data.isSuccess, true)
                assertEquals(data.getOrNull(), expected)
                awaitComplete()
            }
        }

    @Test
    fun `login failed`() =
        runTest {
            val expected = "User not found"
            coEvery { repository.login(any()) } returns
                flow {
                    emit(Result.failure(ApiException(expected)))
                }

            val actual = repository.login(LoginData("", ""))
            actual.test {
                val data = awaitItem()
                assertEquals(data.isSuccess, false)
                assertEquals(data.exceptionOrNull()?.message, expected)
                awaitComplete()
            }
        }

    @Test
    fun `register success`() =
        runTest {
            val expected = "Registration success"
            coEvery { repository.register(any()) } returns
                flow {
                    emit(Result.success(expected))
                }

            val actual = repository.register(RegisterData("", "", "", "", ""))
            actual.test {
                val data = awaitItem()
                assertEquals(data.isSuccess, true)
                assertEquals(data.getOrNull(), expected)
                awaitComplete()
            }
        }

    @Test
    fun `register failed`() =
        runTest {
            val expected = "Registration failed"
            coEvery { repository.register(any()) } returns
                flow {
                    emit(Result.failure(ApiException(expected)))
                }

            val actual = repository.register(RegisterData("", "", "", "", ""))
            actual.test {
                val data = awaitItem()
                assertEquals(data.isSuccess, false)
                assertEquals(data.exceptionOrNull()?.message, expected)
                awaitComplete()
            }
        }
}
