package me.maanraj514.builderdelight.database.sql

import java.sql.Connection
import java.sql.SQLException


class SQLTableBuilder(private val name: String) {

    private val builder = StringBuilder()

    private var primaryKey = false

    init {
        builder.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
    }

    fun addField(name: String, type: DataType, length: Int) : SQLTableBuilder {
        builder.append(name).append(" ").append(type.name).append("(").append(length).append("),");
        return this
    }

    fun setPrimaryKey(name: String?): SQLTableBuilder {
        builder.append("PRIMARY KEY (").append(name).append("))")
        primaryKey = true
        return this
    }

    fun execute(connection: Connection) {
        try {
            connection.prepareStatement(getCommand()).use { stmt ->
                stmt.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getCommand(): String {
        if (!primaryKey) {
            throw NullPointerException("No Primary key specified")
        }
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