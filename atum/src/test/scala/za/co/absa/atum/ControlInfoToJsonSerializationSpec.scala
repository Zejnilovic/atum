/*
 * Copyright 2018-2019 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.absa.atum

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import za.co.absa.atum.model.{Checkpoint, ControlMeasure, ControlMeasureMetadata, Measurement}
import za.co.absa.atum.utils.{BuildProperties, ControlUtils}

/**
  * Unit tests for ControlInfo object serialization
  */
class ControlInfoToJsonSerializationSpec extends AnyFlatSpec with Matchers {
  val exampleCtrlInfo = ControlMeasure(
    metadata = ControlMeasureMetadata(
      sourceApplication = "FrontArena",
      country = "ZA",
      historyType = "Snapshot",
      dataFilename = "example.dat",
      sourceType = "",
      version = 1,
      informationDate = "01-01-2017",
      additionalInfo = Map("key1" -> "value1", "key2" -> "value2")
    ), None,
    checkpoints = List(Checkpoint(
      name = "Source",
      processStartTime = "01-01-2017 08:00:00",
      processEndTime = "01-01-2017 08:00:00",
      workflowName = "Source",
      order = 1,
      controls = List(
        Measurement(
          controlName = "pvControlTotal",
          controlType = "aggregatedTotal",
          controlCol = "pv",
          controlValue = "32847283324.324324"
        ),
        Measurement(
          controlName = "recordCount",
          controlType = "count",
          controlCol = "id",
          controlValue = "243"
        ))
    ).withBuildProperties, Checkpoint(
      name = "Raw",
      processStartTime = "01-01-2017 08:00:00",
      processEndTime = "01-01-2017 08:00:00",
      workflowName = "Raw",
      order = 2,
      controls = List(
        Measurement(
          controlName = "pvControlTotal",
          controlType = "aggregatedTotal",
          controlCol = "pv",
          controlValue = "32847283324.324324"
        ),
        Measurement(
          controlName = "recordCount",
          controlType = "count",
          controlCol = "id",
          controlValue = "243"
        )
      )
    ).withBuildProperties
    )
  )
  val version = BuildProperties.buildVersion
  val exampleInputJson: String = "{\"metadata\":{\"sourceApplication\":\"FrontArena\",\"country\":\"ZA\"," +
    "\"historyType\":\"Snapshot\",\"dataFilename\":\"example.dat\",\"sourceType\":\"\"," +
    "\"version\":1,\"informationDate\":\"01-01-2017\",\"additionalInfo\":{\"key1\":\"value1\",\"key2\":\"value2\"}}," +
    "\"checkpoints\":[{\"name\":\"Source\"," +
    "\"software\":\"Atum\",\"version\":\""+ version + "\"," +
    "\"processStartTime\":\"01-01-2017 08:00:00\"," +
    "\"processEndTime\":\"01-01-2017 08:00:00\",\"workflowName\":\"Source\",\"order\":1," +
    "\"controls\":[{\"controlName\":\"pvControlTotal\",\"controlType\":\"type.aggregatedTotal\"," +
    "\"controlCol\":\"pv\",\"controlValue\":\"32847283324.324324\"},{\"controlName\":\"recordCount\"," +
    "\"controlType\":\"type.Count\",\"controlCol\":\"id\",\"controlValue\":243}]},{\"name\":\"Raw\"," +
    "\"software\":\"Atum\",\"version\":\""+ version + "\"," +
    "\"processStartTime\":\"01-01-2017 08:00:00\",\"processEndTime\":\"01-01-2017 08:00:00\"," +
    "\"workflowName\":\"Raw\",\"order\":2,\"controls\":[{\"controlName\":\"pvControlTotal\"," +
    "\"controlType\":\"type.aggregatedTotal\",\"controlCol\":\"pv\",\"controlValue\":\"32847283324.324324\"}," +
    "{\"controlName\":\"recordCount\",\"controlType\":\"type.Count\",\"controlCol\":\"id\"," +
    "\"controlValue\":243}]}]}"

  val exampleOutputJson: String = "{\"metadata\":{\"sourceApplication\":\"FrontArena\",\"country\":\"ZA\"," +
    "\"historyType\":\"Snapshot\",\"dataFilename\":\"example.dat\",\"sourceType\":\"\"," +
    "\"version\":1,\"informationDate\":\"01-01-2017\",\"additionalInfo\":{\"key1\":\"value1\",\"key2\":\"value2\"}}," +
    "\"checkpoints\":[{\"name\":\"Source\"," +
    "\"software\":\"Atum\",\"version\":\""+ version + "\"," +
    "\"processStartTime\":\"01-01-2017 08:00:00\"," +
    "\"processEndTime\":\"01-01-2017 08:00:00\",\"workflowName\":\"Source\",\"order\":1," +
    "\"controls\":[{\"controlName\":\"pvControlTotal\",\"controlType\":\"aggregatedTotal\"," +
    "\"controlCol\":\"pv\",\"controlValue\":\"32847283324.324324\"},{\"controlName\":\"recordCount\"," +
    "\"controlType\":\"count\",\"controlCol\":\"id\",\"controlValue\":\"243\"}]},{\"name\":\"Raw\"," +
    "\"software\":\"Atum\",\"version\":\""+ version + "\"," +
    "\"processStartTime\":\"01-01-2017 08:00:00\",\"processEndTime\":\"01-01-2017 08:00:00\"," +
    "\"workflowName\":\"Raw\",\"order\":2,\"controls\":[{\"controlName\":\"pvControlTotal\"," +
    "\"controlType\":\"aggregatedTotal\",\"controlCol\":\"pv\",\"controlValue\":\"32847283324.324324\"}," +
    "{\"controlName\":\"recordCount\",\"controlType\":\"count\",\"controlCol\":\"id\"," +
    "\"controlValue\":\"243\"}]}]}"

  "toJson" should "serialize a ControlInfo object" in
  {
    val s = ControlUtils.asJson(exampleCtrlInfo)
    s shouldEqual exampleOutputJson
  }

  "fromJson" should "deserialize a ControlInfo object" in
  {
    val obj = ControlUtils.preprocessControlMeasure( ControlUtils.fromJson[ControlMeasure](exampleInputJson) )
    obj shouldEqual exampleCtrlInfo
  }

  "asJson" should "return the json with control values converted to strings and normalized control type" in
  {
    val obj = ControlUtils.preprocessControlMeasure( ControlUtils.fromJson[ControlMeasure](exampleInputJson) )
    val str = ControlUtils.asJson(obj)
    str shouldEqual exampleOutputJson
  }
}
