package com.yuriikonovalov.helia.di

import com.yuriikonovalov.helia.domain.usecases.AddSearchQueryUseCase
import com.yuriikonovalov.helia.domain.usecases.AddSearchQueryUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.BookHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.BookHotelUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.BookmarkHotelUseCase
import com.yuriikonovalov.helia.domain.usecases.BookmarkHotelUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.CancelBookingUseCase
import com.yuriikonovalov.helia.domain.usecases.CancelBookingUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.ChangePasswordUseCase
import com.yuriikonovalov.helia.domain.usecases.ChangePasswordUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.DeleteProfileUseCase
import com.yuriikonovalov.helia.domain.usecases.DeleteProfileUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.DeleteSearchQueryUseCase
import com.yuriikonovalov.helia.domain.usecases.DeleteSearchQueryUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.FinishOnboardingUseCase
import com.yuriikonovalov.helia.domain.usecases.FinishOnboardingUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetAppPreferencesUseCase
import com.yuriikonovalov.helia.domain.usecases.GetAppPreferencesUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetBookedHotelsUseCase
import com.yuriikonovalov.helia.domain.usecases.GetBookedHotelsUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetBookingPriceUseCase
import com.yuriikonovalov.helia.domain.usecases.GetBookingPriceUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetBookmarkedHotelsUseCase
import com.yuriikonovalov.helia.domain.usecases.GetBookmarkedHotelsUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetHotelCountriesUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelCountriesUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetHotelDetailsUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelDetailsUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetHotelImagesUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelImagesUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetHotelReviewsUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelReviewsUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetRecentlyBookedHotelsUseCase
import com.yuriikonovalov.helia.domain.usecases.GetRecentlyBookedHotelsUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetHotelsByCategoryUseCase
import com.yuriikonovalov.helia.domain.usecases.GetHotelsByCategoryUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetSearchQueriesUseCase
import com.yuriikonovalov.helia.domain.usecases.GetSearchQueriesUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetUserPhotoUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.GetUserPhotoUseCase
import com.yuriikonovalov.helia.domain.usecases.GetUserUseCase
import com.yuriikonovalov.helia.domain.usecases.GetUserUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.IsPasswordChangeAvailableUseCase
import com.yuriikonovalov.helia.domain.usecases.IsPasswordChangeAvailableUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.ObserveUserUseCase
import com.yuriikonovalov.helia.domain.usecases.ObserveUserUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.SearchHotelsUseCase
import com.yuriikonovalov.helia.domain.usecases.SearchHotelsUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.SignInWithEmailAndPasswordUseCase
import com.yuriikonovalov.helia.domain.usecases.SignInWithEmailAndPasswordUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.SignInWithGoogleUseCase
import com.yuriikonovalov.helia.domain.usecases.SignInWithGoogleUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.SignOutUseCase
import com.yuriikonovalov.helia.domain.usecases.SignOutUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.SignUpWithEmailAndPasswordUseCase
import com.yuriikonovalov.helia.domain.usecases.SignUpWithEmailAndPasswordUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.UpdateProfileUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateProfileUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.UpdateRememberMeUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateRememberMeUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.UpdateThemePreferenceUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateThemePreferenceUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.UpdateUserPhotoUseCase
import com.yuriikonovalov.helia.domain.usecases.UpdateUserPhotoUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.ValidateEmailUseCase
import com.yuriikonovalov.helia.domain.usecases.ValidateEmailUseCaseImpl
import com.yuriikonovalov.helia.domain.usecases.ValidatePasswordUseCase
import com.yuriikonovalov.helia.domain.usecases.ValidatePasswordUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindsFinishOnboardingUseCase(impl: FinishOnboardingUseCaseImpl): FinishOnboardingUseCase

    @Binds
    fun bindsUpdateThemePreferenceUseCase(impl: UpdateThemePreferenceUseCaseImpl): UpdateThemePreferenceUseCase

    @Binds
    fun bindsGetAppPreferencesUseCase(impl: GetAppPreferencesUseCaseImpl): GetAppPreferencesUseCase

    @Binds
    fun bindsSignInWithGoogleUseCase(impl: SignInWithGoogleUseCaseImpl): SignInWithGoogleUseCase

    @Binds
    fun bindsSignInWithEmailAndPasswordUseCase(impl: SignInWithEmailAndPasswordUseCaseImpl): SignInWithEmailAndPasswordUseCase

    @Binds
    fun bindsSignUpWithEmailAndPasswordUseCase(impl: SignUpWithEmailAndPasswordUseCaseImpl): SignUpWithEmailAndPasswordUseCase

    @Binds
    fun bindsValidateEmailUseCase(impl: ValidateEmailUseCaseImpl): ValidateEmailUseCase

    @Binds
    fun bindsValidatePasswordUseCase(impl: ValidatePasswordUseCaseImpl): ValidatePasswordUseCase

    @Binds
    fun bindsSignOutUseCase(impl: SignOutUseCaseImpl): SignOutUseCase

    @Binds
    fun bindsUpdateProfileUseCase(impl: UpdateProfileUseCaseImpl): UpdateProfileUseCase

    @Binds
    fun bindsUpdateUserPhotoUseCase(impl: UpdateUserPhotoUseCaseImpl): UpdateUserPhotoUseCase

    @Binds
    fun bindsGetUserPhotoUseCase(impl: GetUserPhotoUseCaseImpl): GetUserPhotoUseCase

    @Binds
    fun bindsGetUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    fun bindsObserveUserUseCase(impl: ObserveUserUseCaseImpl): ObserveUserUseCase

    @Binds
    fun bindsUpdateRememberMeUseCase(impl: UpdateRememberMeUseCaseImpl): UpdateRememberMeUseCase

    @Binds
    fun bindsDeleteProfileUseCase(impl: DeleteProfileUseCaseImpl): DeleteProfileUseCase

    @Binds
    fun bindsChangePasswordUseCase(impl: ChangePasswordUseCaseImpl): ChangePasswordUseCase

    @Binds
    fun bindsIsPasswordChangeAvailableUseCase(impl: IsPasswordChangeAvailableUseCaseImpl): IsPasswordChangeAvailableUseCase

    @Binds
    fun bindsGetHotelsByCategoryUseCase(impl: GetHotelsByCategoryUseCaseImpl): GetHotelsByCategoryUseCase

    @Binds
    fun bindsBookmarkHotelUseCase(impl: BookmarkHotelUseCaseImpl): BookmarkHotelUseCase

    @Binds
    fun bindsGetRecentlyBookedHotelsUseCase(impl: GetRecentlyBookedHotelsUseCaseImpl): GetRecentlyBookedHotelsUseCase

    @Binds
    fun bindsGetBookmarkedHotelsUseCase(impl: GetBookmarkedHotelsUseCaseImpl): GetBookmarkedHotelsUseCase

    @Binds
    fun bindsDeleteSearchQueryUseCase(impl: DeleteSearchQueryUseCaseImpl): DeleteSearchQueryUseCase

    @Binds
    fun bindsGetSearchQueriesUseCase(impl: GetSearchQueriesUseCaseImpl): GetSearchQueriesUseCase

    @Binds
    fun bindsAddSearchQueryUseCase(impl: AddSearchQueryUseCaseImpl): AddSearchQueryUseCase

    @Binds
    fun bindsSearchHotelsUseCase(impl: SearchHotelsUseCaseImpl): SearchHotelsUseCase

    @Binds
    fun bindsGetHotelCountriesUseCase(impl: GetHotelCountriesUseCaseImpl): GetHotelCountriesUseCase

    @Binds
    fun bindsGetHotelDetailsUseCase(impl: GetHotelDetailsUseCaseImpl): GetHotelDetailsUseCase

    @Binds
    fun bindsGetHotelReviewsUseCase(impl: GetHotelReviewsUseCaseImpl): GetHotelReviewsUseCase

    @Binds
    fun bindsGetHotelImagesUseCase(impl: GetHotelImagesUseCaseImpl): GetHotelImagesUseCase

    @Binds
    fun bindsGetBookingPriceUseCase(impl: GetBookingPriceUseCaseImpl): GetBookingPriceUseCase

    @Binds
    fun bindsBookHotelUseCase(impl: BookHotelUseCaseImpl): BookHotelUseCase

    @Binds
    fun bindsGetBookedHotelsUseCase(impl: GetBookedHotelsUseCaseImpl): GetBookedHotelsUseCase

    @Binds
    fun bindsCancelBookingUseCase(impl: CancelBookingUseCaseImpl): CancelBookingUseCase
}