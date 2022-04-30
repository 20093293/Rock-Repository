package controllers

import models.Rock
import persistence.Serializer


class NoteAPI(serializerType: Serializer){

    private var serializer: Serializer = serializerType
    private var rocks = ArrayList<Rock>()

    fun add(rock: Rock): Boolean {
        return rocks.add(rock)
    }

    fun listAllRocks(): String =
        if  (rocks.isEmpty()) "No rocks stored"
        else rocks.joinToString (separator = "\n") { rocks ->
            rocks.indexOf(rocks).toString() + ": " + rocks.toString() }

    fun numberOfRocks(): Int {
        return rocks.size
    }
    fun findRocks(index: Int): Rock? {
        return if (isValidListIndex(index, rocks)) {
            rocks[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun listDisplayedRocks(): String {
        return if (numberOfDisplayedRocks() == 0) {
            "No Displayed Rocks stored"
        } else {
            var listOfDisplayedRocks = ""
            for (rock in rocks) {
                if (!rock.isRockDisplayed) {
                    listOfDisplayedRocks += "${rocks.indexOf(rock)}: $rock \n"
                }
            }
            listOfDisplayedRocks
        }
    }

    fun listUndisplayedRocks(): String {
        return if (numberOfUndisplayedRocks() == 0) {
            "No Undisplayed rocks stored"
        } else {
            var listOfUndisplayedRocks = ""
            for (rock in rocks) {
                if (rock.isRockUndisplayed) {
                    listOfUndisplayedRocks += "${rocks.indexOf(rock)}: $rock \n"
                }
            }
            listOfUndisplayedRocks
        }
    }

    fun numberOfUndisplayedRocks(): Int {
        return rocks.stream()
            .filter{rock: Rock -> !rock.isRockUndisplayed}
            .count()
            .toInt()
    }

    fun numberOfDisplayedRocks(): Int {
        return rocks.stream()
            .filter{rock: Rock -> !rock.isRockDisplayed}
            .count()
            .toInt()
    }

    fun deleteRock(indexToDelete: Int): Rock? {
        return if (isValidListIndex(indexToDelete, rocks)) {
            rocks.removeAt(indexToDelete)
        } else null
    }

    fun updateRock(indexToUpdate: Int, rock: Rock?): Boolean {
        //find the note object by the index number
        val foundRock = findRocks(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundRock != null) && (rock != null)) {
            foundRock.rockType = rock.rockType
            foundRock.rockWeight = rock.rockWeight
            foundRock.locationFound = rock.locationFound
            foundRock.colour = rock.colour
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, rocks);
    }

    @Throws(Exception::class)
    fun load() {
        rocks = serializer.read() as ArrayList<Rock>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(rocks)
    }

    fun undisplayRock(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val rockToUndisplay = rocks[indexToArchive]
            if (!rockToUndisplay.isRockUndisplayed) {
                rockToUndisplay.isRockUndisplayed = true
                return true
            }
        }
        return false
    }
    fun searchByType(searchString : String) =
        rocks.filter { rock -> rock.rockType.contains(searchString, ignoreCase = true)}
            .joinToString (separator = "\n") {
                    rock ->  rocks.indexOf(rock).toString() + ": " + rock.toString() }

}