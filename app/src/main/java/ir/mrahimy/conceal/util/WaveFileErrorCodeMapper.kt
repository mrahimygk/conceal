package ir.mrahimy.conceal.util

import androidx.annotation.StringRes
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.util.Wave.WavFile.*

@StringRes
fun Int.mapToErrorStringRes(): Int {
    return when (this) {
        ILLEGAL_NUMBER_OF_CHANNELS -> R.string.illegal_number_of_channels
        NUMBER_OF_FRAMES_MUST_BE_POSITIVE -> R.string.number_of_frames_must_be_positive
        ILLEGAL_NUMBER_OF_VALID_BITS -> R.string.illegal_number_of_valid_bits
        SAMPLE_RATE_MUST_BE_POSITIVE -> R.string.sample_rate_must_be_positive
        NOT_ENOUGH_WAV_FILE_BYTES_FOR_HEADER -> R.string.not_enough_wav_file_bytes_for_header
        INVALID_WAV_HEADER_DATA_INCORRECT_RIFF_CHUNK_ID -> R.string.invalid_wav_header_data_incorrect_riff_chunk_id
        INVALID_WAV_HEADER_DATA_INCORRECT_RIFF_TYPE_ID -> R.string.invalid_wav_header_data_incorrect_riff_type_id
        HEADER_CHUNK_SIZE_DOES_NOT_MATCH_FILE_SIZE_ -> R.string.header_chunk_size_does_not_match_file_size_
        REACHED_END_OF_FILE_WITHOUT_FINDING_FORMAT_CHUNK -> R.string.reached_end_of_file_without_finding_format_chunk
        COULD_NOT_READ_CHUNK_HEADER -> R.string.could_not_read_chunk_header
        COMPRESSION_CODE_NOT_SUPPORTED -> R.string.compression_code_not_supported
        NUMBER_OF_CHANNELS_SPECIFIED_IN_HEADER_IS_EQUAL_TO_ZERO -> R.string.number_of_channels_specified_in_header_is_equal_to_zero
        BLOCK_ALIGN_SPECIFIED_IN_HEADER_IS_EQUAL_TO_ZERO -> R.string.block_align_specified_in_header_is_equal_to_zero
        VALID_BITS_SPECIFIED_IN_HEADER_IS_LESS_THAN_2 -> R.string.valid_bits_specified_in_header_is_less_than_2
        VALID_BITS_SPECIFIED_IN_HEADER_IS_GREATER_THAN_64 -> R.string.valid_bits_specified_in_header_is_greater_than_64
        BLOCK_ALIGN_DOES_NOT_AGREE_WITH_BYTES_REQUIRED_FOR_VALIDBITS_AND_NUMBER_OF_CHANNELS -> R.string.block_align_does_not_agree_with_bytes_required_for_validbits_and_number_of_channels
        DATA_CHUNK_FOUND_BEFORE_FORMAT_CHUNK -> R.string.data_chunk_found_before_format_chunk
        DATA_CHUNK_SIZE_IS_NOT_MULTIPLE_OF_BLOCK_ALIGN -> R.string.data_chunk_size_is_not_multiple_of_block_align
        DID_NOT_FIND_A_DATA_CHUNK -> R.string.did_not_find_a_data_chunk
        NOT_ENOUGH_DATA_AVAILABLE -> R.string.not_enough_data_available
        else -> R.string.empty
    }
}