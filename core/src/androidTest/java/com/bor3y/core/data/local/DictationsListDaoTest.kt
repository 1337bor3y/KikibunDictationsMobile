package com.bor3y.core.data.local

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bor3y.core.data.local.entity.DictationEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DictationsListDaoTest {

    private lateinit var db: DictationDatabase
    private lateinit var dao: DictationsListDao

    @Before
    fun setUp() {
        // Create an in-memory Room database for testing
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DictationDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.dictationListDao
    }

    @After
    fun tearDown() {
        db.close() // Close the database after tests
    }

    @Test
    fun testUpsertAndGetDictations() = runTest {
        // Arrange: Prepare a list of dictations to insert
        val dictations = listOf(
            DictationEntity(
                id = "64220988-c192-45f5-88a3-f5054b166445",
                title = "New Ways to See",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-24 14:30:58",
                englishLevel = "A1"
            )
        )

        // Act: Insert dictations into the DAO
        dao.upsertDictations(dictations)

        // Load data manually from PagingSource
        val pagingSource = dao.getDictationsByEnglishLevel("A1")
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // Assert: Verify that the inserted dictations match the loaded ones
        val result = (loadResult as PagingSource.LoadResult.Page).data
        assertEquals(dictations.size, result.size)
        assertEquals(dictations[0].id, result[0].id)
        assertEquals(dictations[0].title, result[0].title)
    }

    @Test
    fun testGetDictationsCount() = runTest {
        // Arrange: Insert some dictations
        val dictations = listOf(
            DictationEntity(
                id = "64220988-c192-45f5-88a3-f5054b166445",
                title = "New Ways to See",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-24 14:30:58",
                englishLevel = "A1"
            )
        )
        dao.upsertDictations(dictations)

        // Act: Get the count of dictations
        val count = dao.getDictationsCount()

        // Assert: Verify that the count is correct
        assertEquals(1, count)
    }

    @Test
    fun testDeleteAllDictations() = runTest {
        // Arrange: Insert some dictations
        val dictations = listOf(
            DictationEntity(
                id = "64220988-c192-45f5-88a3-f5054b166445",
                title = "New Ways to See",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-24 14:30:58",
                englishLevel = "A1"
            )
        )
        dao.upsertDictations(dictations)

        // Act: Delete all dictations
        dao.deleteAllDictations()

        // Assert: Verify that the table is empty
        val count = dao.getDictationsCount()
        assertEquals(0, count)
    }

    @Test
    fun testGetOldestDictations() = runTest {
        // Arrange: Insert multiple dictations with different created_at values
        val dictations = listOf(
            DictationEntity(
                id = "1",
                title = "Title 1",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-01 14:30:58",
                englishLevel = "A1"
            ),
            DictationEntity(
                id = "2",
                title = "Title 2",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-02 14:30:58",
                englishLevel = "A2"
            ),
            DictationEntity(
                id = "3",
                title = "Title 3",
                text = "text",
                audioFileDictation = "audioFileDictation",
                audioFileNormal = "audioFileNormal",
                createdAt = "2025-03-03 14:30:58",
                englishLevel = "B1"
            )
        )
        dao.upsertDictations(dictations)

        // Act: Get the 2 oldest dictations
        val result = dao.getOldestDictations(2)

        // Assert: Verify that the oldest dictations are returned (based on createdAt)
        assertEquals(2, result.size)
        assertEquals("Title 1", result[0].title)  // Oldest
        assertEquals("Title 2", result[1].title)  // Second oldest
    }
}
