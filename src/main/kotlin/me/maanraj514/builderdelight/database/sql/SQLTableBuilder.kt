package me.maanraj514.builderdelight.database.sql

import java.sql.Connection
import java.sql.SQLException

class SQLTableBuilder(private val name: String, private val primaryKey: String) {

    private val builder = StringBuilder()

    init {
        builder.append("CREATE TABLE IF NOT EXISTS $name ( PRIMARY KEY ($primaryKey),")
    }

    fun addField(name: String, type: DataType, length: Int) : SQLTableBuilder {
        builder.append("$name ${type.name} ($length),")
        return this
    }

    fun execute(connection: Connection) {
        try {
            connection.prepareStatement(getCommand()).use { stmt -> stmt.executeUpdate() }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun getCommand() : String {
        return builder.toString()
    }

    enum class DataType {
        TINYINT,
        SMALLINT,
        MEDIUMINT,
        INT,
        BIGINT,
        DECIMAL,
        FLOAT,
        DOUBLE,
        REAL,
        BIT,
        BOOLEAN,
        SERIAL,
        DATE,
        DATETIME,
        TIME,
        TIMESTAMP,
        YEAR,
        CHAR,
        VARCHAR,
        TINYTEXT,
        TEXT,
        MEDIUMTEXT,
        LONGTEXT,
        BINARY,
        VARBINARY,
        TINYBLOB,
        BLOB,
        MEDIUMBLOB,
        LONGBLOB,
        ENUM,
        SET,
        GEOMETRY,
        POINT,
        LINESTRING,
        POLYGON,
        MULTIPOINT,
        MULTILINESTRING,
        MULTIPOLYGON,
        GEOMETRYCOLLECTION,
        JSON
    }
}