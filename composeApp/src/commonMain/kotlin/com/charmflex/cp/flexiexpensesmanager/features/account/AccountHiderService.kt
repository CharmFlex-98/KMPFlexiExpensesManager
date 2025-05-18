import com.charmflex.cp.flexiexpensesmanager.features.account.data.storage.AccountStorage

internal interface AccountHiderService {
    suspend fun toggleHideAccount(isHidden: Boolean)

    suspend fun isAccountHidden(): Boolean
}

internal class AccountHiderServiceImpl (
    private val accountStorage: AccountStorage
) : AccountHiderService {
    override suspend fun toggleHideAccount(isHidden: Boolean) {
        if (isHidden) accountStorage.hideAccountInfo()
        else accountStorage.unHideAccountInfo()
    }

    override suspend fun isAccountHidden(): Boolean {
        return accountStorage.getAccountInfoHidden()
    }
}