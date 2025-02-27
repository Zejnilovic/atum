package za.co.absa.atum.persistence.hdfs

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import za.co.absa.atum.persistence.TestResources
import za.co.absa.atum.utils.{FileUtils, HdfsFileUtils}

class ControlMeasuresHdfsStorerJsonSpec extends AnyFlatSpec with Matchers {

  val expectedFilePath: String = TestResources.InputInfo.localPath
  val inputControlMeasure = TestResources.InputInfo.controlMeasure

  val hadoopConfiguration = new Configuration()
  implicit val fs = FileSystem.get(hadoopConfiguration)

  "ControlMeasuresHdfsStorerJsonFile" should "store json file to HDFS" in {

    val outputPath = new Path("/tmp/json-hdfs-storing-test")
    fs.delete(outputPath, false)

    ControlMeasuresHdfsStorerJsonFile(outputPath).store(inputControlMeasure)

    val actualContent = HdfsFileUtils.readHdfsFileToString(outputPath)
    val expectedContent = FileUtils.readFileToString(expectedFilePath)

    // some output may be prettified while other may not, we do not take this into account.
    TestResources.filterWhitespaces(actualContent) shouldBe TestResources.filterWhitespaces(expectedContent)

    fs.delete(outputPath, false)
  }

}
