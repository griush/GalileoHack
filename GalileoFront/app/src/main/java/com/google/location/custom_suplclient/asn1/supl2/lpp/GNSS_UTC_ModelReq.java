// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.location.custom_suplclient.asn1.supl2.lpp;

// Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//
//
import com.google.location.custom_suplclient.asn1.base.Asn1Integer;
import com.google.location.custom_suplclient.asn1.base.Asn1Object;
import com.google.location.custom_suplclient.asn1.base.Asn1Sequence;
import com.google.location.custom_suplclient.asn1.base.Asn1Tag;
import com.google.location.custom_suplclient.asn1.base.BitStream;
import com.google.location.custom_suplclient.asn1.base.BitStreamReader;
import com.google.location.custom_suplclient.asn1.base.SequenceComponent;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nullable;


/**
* 
*/
public  class GNSS_UTC_ModelReq extends Asn1Sequence {
  //

  private static final Asn1Tag TAG_GNSS_UTC_ModelReq
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public GNSS_UTC_ModelReq() {
    super();
  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_GNSS_UTC_ModelReq;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_GNSS_UTC_ModelReq != null) {
      return ImmutableList.of(TAG_GNSS_UTC_ModelReq);
    } else {
      return Asn1Sequence.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new GNSS_UTC_ModelReq from encoded stream.
   */
  public static GNSS_UTC_ModelReq fromPerUnaligned(byte[] encodedBytes) {
    GNSS_UTC_ModelReq result = new GNSS_UTC_ModelReq();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new GNSS_UTC_ModelReq from encoded stream.
   */
  public static GNSS_UTC_ModelReq fromPerAligned(byte[] encodedBytes) {
    GNSS_UTC_ModelReq result = new GNSS_UTC_ModelReq();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }



  @Override protected boolean isExtensible() {
    return true;
  }

  @Override public boolean containsExtensionValues() {
    for (SequenceComponent extensionComponent : getExtensionComponents()) {
      if (extensionComponent.isExplicitlySet()) return true;
    }
    return false;
  }

  
  private GNSS_UTC_ModelReq.modelIDType modelID_;
  public GNSS_UTC_ModelReq.modelIDType getModelID() {
    return modelID_;
  }
  /**
   * @throws ClassCastException if value is not a GNSS_UTC_ModelReq.modelIDType
   */
  public void setModelID(Asn1Object value) {
    this.modelID_ = (GNSS_UTC_ModelReq.modelIDType) value;
  }
  public GNSS_UTC_ModelReq.modelIDType setModelIDToNewInstance() {
    modelID_ = new GNSS_UTC_ModelReq.modelIDType();
    return modelID_;
  }
  

  

  

  @Override public Iterable<? extends SequenceComponent> getComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
    
    builder.add(new SequenceComponent() {
          Asn1Tag tag = Asn1Tag.fromClassAndNumber(2, 0);

          @Override public boolean isExplicitlySet() {
            return getModelID() != null;
          }

          @Override public boolean hasDefaultValue() {
            return false;
          }

          @Override public boolean isOptional() {
            return true;
          }

          @Override public Asn1Object getComponentValue() {
            return getModelID();
          }

          @Override public void setToNewInstance() {
            setModelIDToNewInstance();
          }

          @Override public Collection<Asn1Tag> getPossibleFirstTags() {
            return tag == null ? GNSS_UTC_ModelReq.modelIDType.getPossibleFirstTags() : ImmutableList.of(tag);
          }

          @Override
          public Asn1Tag getTag() {
            return tag;
          }

          @Override
          public boolean isImplicitTagging() {
            return true;
          }

          @Override public String toIndentedString(String indent) {
                return "modelID : "
                    + getModelID().toIndentedString(indent);
              }
        });
    
    return builder.build();
  }

  @Override public Iterable<? extends SequenceComponent>
                                                    getExtensionComponents() {
    ImmutableList.Builder<SequenceComponent> builder = ImmutableList.builder();
      
      return builder.build();
    }

  
  // Copyright 2008 Google Inc. All Rights Reserved.
/*
 * This class is AUTOMATICALLY GENERATED. Do NOT EDIT.
 */


//

/**
 * 
 */
public static class modelIDType extends Asn1Integer {
  //

  private static final Asn1Tag TAG_modelIDType
      = Asn1Tag.fromClassAndNumber(-1, -1);

  public modelIDType() {
    super();
    setValueRange(new java.math.BigInteger("1"), new java.math.BigInteger("8"));

  }

  @Override
  @Nullable
  protected Asn1Tag getTag() {
    return TAG_modelIDType;
  }

  @Override
  protected boolean isTagImplicit() {
    return true;
  }

  public static Collection<Asn1Tag> getPossibleFirstTags() {
    if (TAG_modelIDType != null) {
      return ImmutableList.of(TAG_modelIDType);
    } else {
      return Asn1Integer.getPossibleFirstTags();
    }
  }

  /**
   * Creates a new modelIDType from encoded stream.
   */
  public static modelIDType fromPerUnaligned(byte[] encodedBytes) {
    modelIDType result = new modelIDType();
    result.decodePerUnaligned(new BitStreamReader(encodedBytes));
    return result;
  }

  /**
   * Creates a new modelIDType from encoded stream.
   */
  public static modelIDType fromPerAligned(byte[] encodedBytes) {
    modelIDType result = new modelIDType();
    result.decodePerAligned(new BitStreamReader(encodedBytes));
    return result;
  }

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    return "modelIDType = " + getInteger() + ";\n";
  }
}

  

    

  @Override public Iterable<BitStream> encodePerUnaligned() {
    return super.encodePerUnaligned();
  }

  @Override public Iterable<BitStream> encodePerAligned() {
    return super.encodePerAligned();
  }

  @Override public void decodePerUnaligned(BitStreamReader reader) {
    super.decodePerUnaligned(reader);
  }

  @Override public void decodePerAligned(BitStreamReader reader) {
    super.decodePerAligned(reader);
  }

  @Override public String toString() {
    return toIndentedString("");
  }

  public String toIndentedString(String indent) {
    StringBuilder builder = new StringBuilder();
    builder.append("GNSS_UTC_ModelReq = {\n");
    final String internalIndent = indent + "  ";
    for (SequenceComponent component : getComponents()) {
      if (component.isExplicitlySet()) {
        builder.append(internalIndent)
            .append(component.toIndentedString(internalIndent));
      }
    }
    if (isExtensible()) {
      builder.append(internalIndent).append("...\n");
      for (SequenceComponent component : getExtensionComponents()) {
        if (component.isExplicitlySet()) {
          builder.append(internalIndent)
              .append(component.toIndentedString(internalIndent));
        }
      }
    }
    builder.append(indent).append("};\n");
    return builder.toString();
  }
}
